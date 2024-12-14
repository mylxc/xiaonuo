/*
 * Copyright [2022] [https://www.jcxxdd.com]
 6.若您的项目无法满足以上几点，需要更多功能代码，获取Jackli商业授权许可，请在官网购买授权，地址为 https://www.jcxxdd.com
 */
package com.jackli.common.pojo;

import com.jackli.common.util.I18nUtil;
import io.swagger.annotations.ApiModelProperty;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ResponseMessage;
import com.jackli.common.enums.CommonExceptionEnum;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 对Ajax请求返回Json格式数据的简易封装
 *
 * @author lijinchang
 * @date 2022/8/15 16:08
 **/
public class CommonResult<T> implements Serializable{
    private static final long serialVersionUID = 1L;
    public static final int CODE_SUCCESS = 200;
    public static final int CODE_ERROR = 500;

    @ApiModelProperty(value = "状态码")
    private int code;

    @ApiModelProperty(value = "提示语")
    private String msg;

    @ApiModelProperty(value = "返回数据")
    private T data;

    public CommonResult() {
    }

    public CommonResult(int code, String msg, T data) {
        this.setCode(code);
        this.setMsg(msg);
        this.setData(data);
    }

    /**
     * 获取code
     * @return code
     */
    public Integer getCode() {
        return this.code;
    }

    /**
     * 获取msg
     * @return msg
     */
    public String getMsg() {
        return this.msg;
    }
    /**
     * 获取data
     * @return data
     */
    public T getData() {
        return this.data;
    }

    /**
     * 给code赋值，连缀风格
     * @param code code
     * @return 对象自身
     */
    public CommonResult<T> setCode(int code) {
        this.code = code;
        return this;
    }

    /**
     * 给msg赋值，连缀风格
     * @param msg msg
     * @return 对象自身
     */
    public CommonResult<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    /**
     * 给data赋值，连缀风格
     * @param data data
     * @return 对象自身
     */
    public CommonResult<T> setData(T data) {
        this.data = data;
        return this;
    }


    // ============================  构建  ==================================

    // 构建成功
    public static <T> CommonResult<T> ok() {
        return new CommonResult<>(CODE_SUCCESS, I18nUtil.getMessage("10006", "操作成功"), null);
    }
    public static <T> CommonResult<T> ok(String msg) {
        return new CommonResult<>(CODE_SUCCESS, msg, null);
    }
    public static <T> CommonResult<T> code(int code) {
        return new CommonResult<>(code, null, null);
    }

    public static <T> CommonResult<T> code(int code, String msg) {
        return new CommonResult<>(code, msg, null);
    }
    public static <T> CommonResult<T> data(T data) {
        return new CommonResult<>(CODE_SUCCESS, I18nUtil.getMessage("10006", "操作成功"), data);
    }

    // 构建失败
    public static <T> CommonResult<T> error() {
        return new CommonResult<>(CODE_ERROR, I18nUtil.getMessage("10001", "操作成功"), null);
    }
    public static <T> CommonResult<T> error(String msg) {
        return new CommonResult<>(CODE_ERROR, msg, null);
    }

    // 构建指定状态码
    public static <T> CommonResult<T> get(int code, String msg, T data) {
        return new CommonResult<>(code, msg, data);
    }

    /*
     * toString()
     */
    @Override
    public String toString() {
        return "{"
                + "\"code\": " + this.getCode()
                + ", \"msg\": \"" + this.getMsg() + "\""
                + ", \"data\": \"" + this.getData() + "\""
                + "}";
    }

    /**
     * 响应状态码集合
     *
     * @author lijinchang
     * @date 2022/7/25 13:36
     **/
    public static List<ResponseMessage> responseList() {
        return Arrays.stream(CommonExceptionEnum.values()).map(commonExceptionEnum -> new ResponseMessageBuilder()
                .code(commonExceptionEnum.getCode()).message(commonExceptionEnum.getMessage()).build())
                .collect(Collectors.toList());
    }
}
