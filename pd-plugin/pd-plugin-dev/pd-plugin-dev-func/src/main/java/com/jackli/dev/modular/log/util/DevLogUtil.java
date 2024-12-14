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
package com.jackli.dev.modular.log.util;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.jackli.common.annotation.CommonLog;
import com.jackli.common.annotation.ConsoleLog;
import com.jackli.common.util.*;
import com.jackli.dev.modular.log.entity.DevLog;
import com.jackli.dev.modular.log.enums.DevLogCategoryEnum;
import com.jackli.dev.modular.log.enums.DevLogExeStatusEnum;
import com.jackli.dev.modular.log.service.DevLogService;
import com.jackli.ten.api.TenApi;
import org.aspectj.lang.JoinPoint;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

/**
 * 日志工具类
 *
 * @author lijinchang
 * @date 2022/9/2 15:26
 */
public class DevLogUtil {

    private static final DevLogService devLogService = SpringUtil.getBean(DevLogService.class);

    private static final TenApi tenApi = SpringUtil.getBean(TenApi.class);
    /**
     * 记录操作日志
     *
     * @author lijinchang
     * @date 2022/9/2 15:31
     */
    public static void executeConsoleOperationLog(ConsoleLog consoleLog, String userName, JoinPoint joinPoint, String resultJson) {
        HttpServletRequest request = CommonServletUtil.getRequest();
        DevLog devLog = genBasOpLog();
        devLog.setTenantId(tenApi.getCurrentTenId());
        devLog.setCategory(DevLogCategoryEnum.CONSOLE.getValue());
        devLog.setName(consoleLog.value());
        devLog.setExeStatus(DevLogExeStatusEnum.SUCCESS.getValue());
        devLog.setClassName(joinPoint.getTarget().getClass().getName());
        devLog.setMethodName(joinPoint.getSignature().getName());
        devLog.setReqUrl(request.getRequestURI());
        String parmLogStr=CommonJoinPointUtil.getArgsJsonString(joinPoint);
        if(ObjectUtil.isNotEmpty(parmLogStr)) {
            if(parmLogStr.length()>1000) {
                devLog.setParamJson(parmLogStr.substring(0, 1000));
            }
            else {
                devLog.setParamJson(parmLogStr);
            }
        }
        devLog.setResultJson(resultJson);
        devLog.setOpTime(DateTime.now());
        devLog.setOpUser(userName);
        creatLogSignValue(devLog);
        devLogService.save(devLog);
    }

    /**
     * 记录操作日志
     *
     * @author lijinchang
     * @date 2022/9/2 15:31
     */
    public static void executeOperationLog(CommonLog commonLog, String userName, JoinPoint joinPoint, String resultJson) {
        HttpServletRequest request = CommonServletUtil.getRequest();
        DevLog devLog = genBasOpLog();
        devLog.setTenantId(tenApi.getCurrentTenId());
        devLog.setCategory(DevLogCategoryEnum.OPERATE.getValue());
        devLog.setName(commonLog.value());
        devLog.setExeStatus(DevLogExeStatusEnum.SUCCESS.getValue());
        devLog.setClassName(joinPoint.getTarget().getClass().getName());
        devLog.setMethodName(joinPoint.getSignature().getName());
        devLog.setReqUrl(request.getRequestURI());
        String parmLogStr=CommonJoinPointUtil.getArgsJsonString(joinPoint);
        if(ObjectUtil.isNotEmpty(parmLogStr)) {
            if(parmLogStr.length()>1000) {
                devLog.setParamJson(parmLogStr.substring(0, 1000));
            }
            else {
                devLog.setParamJson(parmLogStr);
            }
        }
        devLog.setResultJson(resultJson);
        devLog.setOpTime(DateTime.now());
        devLog.setOpUser(userName);
        creatLogSignValue(devLog);
        devLogService.save(devLog);
    }

    private static byte[] compress(String data) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(byteArrayOutputStream, new Deflater())) {
            deflaterOutputStream.write(data.getBytes());
        }
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * 记录异常日志
     *
     * @author lijinchang
     * @date 2022/9/2 15:31
     */
    public static void executeConsoleExceptionLog(ConsoleLog consoleLog, String userName, JoinPoint joinPoint, Exception exception) {
        HttpServletRequest request = CommonServletUtil.getRequest();
        DevLog devLog = genBasOpLog();
        devLog.setTenantId(tenApi.getCurrentTenId());
        devLog.setCategory(DevLogCategoryEnum.CONSOLEEXCEPTION.getValue());
        devLog.setName(consoleLog.value());
        devLog.setExeStatus(DevLogExeStatusEnum.FAIL.getValue());
        devLog.setExeMessage(ExceptionUtil.stacktraceToString(exception, 10000));
        devLog.setClassName(joinPoint.getTarget().getClass().getName());
        devLog.setMethodName(joinPoint.getSignature().getName());
        devLog.setReqMethod(request.getMethod());
        devLog.setReqUrl(request.getRequestURI());
        devLog.setParamJson(CommonJoinPointUtil.getArgsJsonString(joinPoint));
        devLog.setOpTime(DateTime.now());
        devLog.setOpUser(userName);
        creatLogSignValue(devLog);
        devLogService.save(devLog);
    }

    /**
     * 记录异常日志
     *
     * @author lijinchang
     * @date 2022/9/2 15:31
     */
    public static void executeExceptionLog(CommonLog commonLog, String userName, JoinPoint joinPoint, Exception exception) {
        HttpServletRequest request = CommonServletUtil.getRequest();
        DevLog devLog = genBasOpLog();
        devLog.setTenantId(tenApi.getCurrentTenId());
        devLog.setCategory(DevLogCategoryEnum.EXCEPTION.getValue());
        devLog.setName(commonLog.value());
        devLog.setExeStatus(DevLogExeStatusEnum.FAIL.getValue());
        devLog.setExeMessage(ExceptionUtil.stacktraceToString(exception, 10000));
        devLog.setClassName(joinPoint.getTarget().getClass().getName());
        devLog.setMethodName(joinPoint.getSignature().getName());
        devLog.setReqMethod(request.getMethod());
        devLog.setReqUrl(request.getRequestURI());
        devLog.setParamJson(CommonJoinPointUtil.getArgsJsonString(joinPoint));
        devLog.setOpTime(DateTime.now());
        devLog.setOpUser(userName);
        creatLogSignValue(devLog);
        devLogService.save(devLog);
    }

    /**
     * 记录登录日志
     *
     * @author lijinchang
     * @date 2022/9/2 16:08
     */
    public static void executeLoginLog(String userName) {
        DevLog devLog = genBasOpLog();
        devLog.setTenantId(tenApi.getCurrentTenId());
        devLog.setCategory(DevLogCategoryEnum.LOGIN.getValue());
        devLog.setName("用户登录");
        devLog.setExeStatus(DevLogExeStatusEnum.SUCCESS.getValue());
        devLog.setOpTime(DateTime.now());
        devLog.setOpUser(userName);
        creatLogSignValue(devLog);
        devLogService.save(devLog);
    }

    /**
     * 记录登出日志
     *
     * @author lijinchang
     * @date 2022/9/2 16:08
     */
    public static void executeLogoutLog(String userName) {
        DevLog devLog = genBasOpLog();
        devLog.setTenantId(tenApi.getCurrentTenId());
        devLog.setCategory(DevLogCategoryEnum.LOGOUT.getValue());
        devLog.setName("用户登出");
        devLog.setExeStatus(DevLogExeStatusEnum.SUCCESS.getValue());
        devLog.setOpTime(DateTime.now());
        devLog.setOpUser(userName);
        creatLogSignValue(devLog);
        devLogService.save(devLog);
    }

    /**
     * 构建基础操作日志
     *
     * @author lijinchang
     * @date 2020/3/19 14:44
     */
    private static DevLog genBasOpLog() {
        HttpServletRequest request = CommonServletUtil.getRequest();
        String ip = CommonIpAddressUtil.getIp(request);
        DevLog devLog = new DevLog();
        devLog.setOpIp(CommonIpAddressUtil.getIp(request));
        devLog.setOpAddress(CommonIpAddressUtil.getCityInfo(ip));
        devLog.setOpBrowser(CommonUaUtil.getBrowser(request));
        devLog.setOpOs(CommonUaUtil.getOs(request));
        return devLog;
    }

    /**
     * 构建日志完整性保护签名数据
     */
    private static void creatLogSignValue (DevLog devLog) {
        String logStr = devLog.toString().replaceAll(" +","");
        devLog.setSignData(CommonCryptogramUtil.doSignature(logStr));
    }
}
