/*
 * Copyright [2022] [https://www.jcxxdd.com]
 *
 * Jackli采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Jackli源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.jcxxdd.com
 * 5.不可二次分发开源参与同类竞品，如有想法可联系团队767076381@qq.com商议合作。
 * 6.若您的项目无法满足以上几点，需要更多功能代码，获取Jackli商业授权许可，请在官网购买授权，地址为 https://www.jcxxdd.com
 */
package com.jackli.web.core.handler;

import cn.dev33.satoken.exception.SaTokenException;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import com.jackli.common.http.exception.Message;
import com.jackli.common.util.I18nUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.PersistenceException;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import com.jackli.auth.core.util.AuthExceptionUtil;
import com.jackli.common.exception.CommonException;
import com.jackli.common.pojo.CommonResult;
import com.jackli.common.util.CommonServletUtil;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 全局异常处理工具类，将异常转为通用结果
 *
 * @author lijinchang
 * @date 2021/12/18 16:44
 */
@Slf4j
public class GlobalExceptionUtil {

    /**
     * 根据错误类型获取对应的CommonResult
     *
     * @author lijinchang
     * @date 2021/10/11 15:52
     **/
    public static CommonResult<String> getCommonResult(Exception e) {
        CommonResult<String> commonResult;
        if (e instanceof HttpRequestMethodNotSupportedException) {

            // 如果是请求方法异常 405
            String method = CommonServletUtil.getRequest().getMethod();
            if (HttpMethod.GET.toString().equals(method)) {
                commonResult = CommonResult.get(HttpStatus.HTTP_BAD_METHOD,
                        I18nUtil.getMessage(Message.HTTP_BAD_METHOD_GET, "请求方法应为POST"), null);
            } else if(HttpMethod.POST.toString().equals(method)) {
                commonResult = CommonResult.get(HttpStatus.HTTP_BAD_METHOD,
                        I18nUtil.getMessage(Message.HTTP_BAD_METHOD_POST, "请求方法应为GET"), null);
            } else {
                commonResult = CommonResult.get(HttpStatus.HTTP_BAD_METHOD, I18nUtil.getMessage(Message.HTTP_BAD_METHOD, "请求方法仅支持GET或POST"), null);
            }
        } else if (e instanceof HttpMessageNotReadableException) {

            // 如果是参数传递格式不支持异常 415
            if (e.getMessage().contains("JSON parse error")) {
                e.printStackTrace();
                //JSON格式转换错误特殊提示
                commonResult = CommonResult.get(HttpStatus.HTTP_UNSUPPORTED_TYPE,  I18nUtil.getMessage(Message.PARSE_ERROR, "参数格式错误"), null);
            } else {
                commonResult = CommonResult.get(HttpStatus.HTTP_UNSUPPORTED_TYPE, I18nUtil.getMessage(Message.NON_JSON_FORMAT, "请使用JSON方式传参"), null);
            }
        } else if (e instanceof HttpMediaTypeNotSupportedException) {
            e.printStackTrace();
            // 如果是JSON参数格式错误异常 415
            commonResult = CommonResult.get(HttpStatus.HTTP_UNSUPPORTED_TYPE, I18nUtil.getMessage(Message.PARSE_ERROR, "参数格式错误"), null);
        } else if (e instanceof MethodArgumentNotValidException) {

            // 如果是参数校验异常（MethodArgumentNotValidException） 415
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) e;
            commonResult = CommonResult.get(HttpStatus.HTTP_UNSUPPORTED_TYPE, getI18nValidMessage(methodArgumentNotValidException.getBindingResult()), null);
        } else if (e instanceof BindException) {

            // 如果是参数校验异常（BindException） 415
            BindException bindException = (BindException) e;
            commonResult = CommonResult.get(HttpStatus.HTTP_UNSUPPORTED_TYPE, getArgNotValidMessage(bindException.getBindingResult()), null);
        } else if (e instanceof ConstraintViolationException) {

            // 如果是参数校验异常（ConstraintViolationException） 415
            ConstraintViolationException constraintViolationException = (ConstraintViolationException) e;
            commonResult = CommonResult.get(HttpStatus.HTTP_UNSUPPORTED_TYPE, getArgNotValidMessage(constraintViolationException.getConstraintViolations()), null);
        } else if (e instanceof MissingServletRequestParameterException) {

            // 如果是参数校验异常（MissingServletRequestParameterException） 415
            MissingServletRequestParameterException missingServletRequestParameterException = (MissingServletRequestParameterException) e;
            commonResult = CommonResult.get(HttpStatus.HTTP_UNSUPPORTED_TYPE, missingServletRequestParameterException.getMessage(), null);
        }
        else if (e instanceof MultipartException) {

            //文件上传错误特殊提示
            commonResult = CommonResult.error(I18nUtil.getMessage(Message.MULTIPART_ERROR, "请使用multipart/form-data方式上传文件"));
        } else if (e instanceof MissingServletRequestPartException) {

            //文件上传错误特殊提示
            commonResult = CommonResult.error(I18nUtil.getMessage(Message.MISSING_SERVLET_REQUEST_PART, "请选择要上传的文件并检查文件参数名称是否正确"));
        } else if (e instanceof SaTokenException) {

            // 如果是SaToken相关异常，则由AuthExceptionUtil处理
            return AuthExceptionUtil.getCommonResult(e);
        } else if(e instanceof MyBatisSystemException) {

            // 如果是MyBatisSystemException
            Throwable cause = e.getCause();
            if (cause instanceof PersistenceException) {
                Throwable secondCause = cause.getCause();
                if (secondCause instanceof CommonException) {
                    CommonException commonException = (CommonException) secondCause;
                    commonResult = CommonResult.get(commonException.getCode(), commonException.getMsg(), null);
                } else {
                    e.printStackTrace();
                    commonResult = CommonResult.error(I18nUtil.getMessage(Message.PERSISTENCE_ERROR, "数据操作异常"));
                }
            }else {
                e.printStackTrace();
                commonResult = CommonResult.error(I18nUtil.getMessage(Message.PERSISTENCE_ERROR, "数据操作异常"));
            }
        } else if (e instanceof CommonException) {

            // 通用业务异常，直接返回给前端
            CommonException commonException = (CommonException) e;
            String code = I18nUtil.getCodeFromValue(commonException.getMessage());
            commonResult = CommonResult.get(commonException.getCode(),
                    StrUtil.format(I18nUtil.getMessage(code, commonException.getMessage()), commonException.getArguments()), null);
        }  else {
            // 未知异常打印详情
            e.printStackTrace();

            // 未知异常返回服务器异常
            commonResult = CommonResult.error(I18nUtil.getMessage(Message.SERVER_ERROR, "服务器异常"));
        }
        log.error(">>> {}，请求地址：{}", commonResult.getMsg(), CommonServletUtil.getRequest().getRequestURL());

        // 如果信息为空 并且code不为200
        if (StrUtil.isBlank(commonResult.getMsg()) && commonResult.getCode() != 200) {
            commonResult.setMsg(I18nUtil.getMessage(Message.RESPONSE_PARAMETER_IS_EMPTY, "响应参数为空"));
        }
        return commonResult;
    }

    /**
     * 获取请求参数不正确的提示信息，多个信息，拼接成用逗号分隔的形式
     *
     * @author lijinchang
     * @date 2021/10/12 11:14
     **/
    public static String getArgNotValidMessage(Set<ConstraintViolation<?>> constraintViolationSet) {
        if (ObjectUtil.isEmpty(constraintViolationSet)) {
            return "";
        }
        StringBuilder stringBuilder = StrUtil.builder();

        // 多个错误用逗号分隔
        for (ConstraintViolation<?> constraintViolation : constraintViolationSet) {
            stringBuilder.append(StrUtil.COMMA).append(constraintViolation.getMessage());
        }

        // 最终把首部的逗号去掉
        return StrUtil.removePrefix(stringBuilder.toString(), StrUtil.COMMA);
    }

    /**
     * 获取请求参数不正确的提示信息，多个信息，拼接成用逗号分隔的形式
     *
     * @author lijinchang
     * @date 2021/10/12 11:14
     **/
    public static String getArgNotValidMessage(BindingResult bindingResult) {
        if (ObjectUtil.isNull(bindingResult)) {
            return "";
        }
        StringBuilder stringBuilder = StrUtil.builder();

        // 多个错误用逗号分隔
        List<ObjectError> allErrorInfos = bindingResult.getAllErrors();
        for (ObjectError error : allErrorInfos) {
            stringBuilder.append(StrUtil.COMMA).append(error.getDefaultMessage());
        }

        // 最终把首部的逗号去掉
        return StrUtil.removePrefix(stringBuilder.toString(), StrUtil.COMMA);
    }

    public static String getI18nValidMessage(BindingResult bindingResult) {
        if (ObjectUtil.isNull(bindingResult)) {
            return I18nUtil.getMessage(Message.PERSISTENCE_ERROR, "数据操作异常");
        }
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (CollectionUtil.isEmpty(fieldErrors)) {
            return I18nUtil.getMessage(Message.PERSISTENCE_ERROR, "数据操作异常");
        }

        // 获取错误的单个字段
        FieldError filer = fieldErrors.get(0);
        if (null != filer) {
            String validaCode = filer.getCode();
            // 最大最小值错误
            if ("Min".equals(validaCode) || "Max".equals(validaCode)) {
                Object num = Objects.requireNonNull(filer.getArguments())[1];
                return I18nUtil.getFilerMessage(filer.getField()) + StrUtil.format(I18nUtil.getValidatorMessage(validaCode), num);
            }
            // 集合的范围错误
            else if ("Range".equals(validaCode) || "Size".equals(validaCode) || "Length".equals(validaCode)) {
                Object max = Objects.requireNonNull(filer.getArguments())[1];
                Object min = Objects.requireNonNull(filer.getArguments())[2];
                return I18nUtil.getFilerMessage(filer.getField()) + StrUtil.format(I18nUtil.getValidatorMessage(validaCode), min, max);
            }
            return I18nUtil.getFilerMessage(filer.getField()) + I18nUtil.getValidatorMessage(validaCode);
        }

        String objectName = bindingResult.getObjectName();
        String message = I18nUtil.getMessage(Message.PARAMETER_NULL, "参数：{} 不能为空");
        return StrUtil.format(message,objectName);
    }

}
