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
package com.jackli.dev.modular.log.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 日志实体
 *
 * @author lijinchang
 * @date 2022/9/2 15:02
 */
@Getter
@Setter
@TableName("DEV_LOG")
public class DevLog {

    /** id */
    @ApiModelProperty(value = "主键", position = 1)
    private String id;

    /** 租户id */
    @ApiModelProperty(value = "租户id", position = 2)
    private String tenantId;

    /** 日志分类 */
    @ApiModelProperty(value = "日志分类", position = 3)
    private String category;

    /** 日志名称 */
    @ApiModelProperty(value = "日志名称", position = 4)
    private String name;

    /** 执行状态 */
    @ApiModelProperty(value = "执行状态", position = 5)
    private String exeStatus;

    /** 具体消息 */
    @ApiModelProperty(value = "具体消息", position = 6)
    private String exeMessage;

    /** 操作ip */
    @ApiModelProperty(value = "操作ip", position = 7)
    private String opIp;

    /** 操作地址 */
    @ApiModelProperty(value = "操作地址", position = 8)
    private String opAddress;

    /** 操作浏览器 */
    @ApiModelProperty(value = "操作浏览器", position = 9)
    private String opBrowser;

    /** 操作系统 */
    @ApiModelProperty(value = "操作系统", position = 10)
    private String opOs;

    /** 类名称 */
    @ApiModelProperty(value = "类名称", position = 11)
    private String className;

    /** 方法名称 */
    @ApiModelProperty(value = "方法名称", position = 12)
    private String methodName;

    /** 请求方式 */
    @ApiModelProperty(value = "请求方式", position = 13)
    private String reqMethod;

    /** 请求地址 */
    @ApiModelProperty(value = "请求地址", position = 14)
    private String reqUrl;

    /** 请求参数 */
    @ApiModelProperty(value = "请求参数", position = 15)
    private String paramJson;

    /** 返回结果 */
    @ApiModelProperty(value = "返回结果", position = 16)
    private String resultJson;

    /** 操作时间 */
    @ApiModelProperty(value = "操作时间", position = 17)
    private Date opTime;

    /** 操作人姓名 */
    @ApiModelProperty(value = "操作人姓名", position = 18)
    private String opUser;

    /** 签名数据 */
    @ApiModelProperty(value = "签名数据", position = 19)
    private String signData;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间", position = 20)
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /** 创建人 */
    @ApiModelProperty(value = "创建人", position = 21)
    @TableField(fill = FieldFill.INSERT)
    private String createUser;

    /** 更新时间 */
    @ApiModelProperty(value = "更新时间", position = 22)
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    /** 更新人 */
    @ApiModelProperty(value = "更新人", position = 23)
    @TableField(fill = FieldFill.UPDATE)
    private String updateUser;
}
