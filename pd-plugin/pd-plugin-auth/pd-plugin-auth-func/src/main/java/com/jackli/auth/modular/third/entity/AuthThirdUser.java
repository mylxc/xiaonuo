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
package com.jackli.auth.modular.third.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import com.jackli.common.pojo.CommonEntity;

/**
 * 第三放登录实体
 *
 * @author lijinchang
 * @date 2022/7/9 14:29
 */
@Getter
@Setter
@TableName("AUTH_THIRD_USER")
public class AuthThirdUser extends CommonEntity {

    /** id */
    @ApiModelProperty(value = "id", position = 1)
    private String id;

    /** 租户id */
    @ApiModelProperty(value = "租户id", position = 2)
    private String tenantId;

    /** 三方用户id */
    @ApiModelProperty(value = "三方用户id", position = 3)
    private String thirdId;

    /** 系统用户id */
    @ApiModelProperty(value = "系统用户id", position = 4)
    private String userId;

    /** 头像 */
    @ApiModelProperty(value = "头像", position = 5)
    @TableField(insertStrategy = FieldStrategy.IGNORED, updateStrategy = FieldStrategy.IGNORED)
    private String avatar;

    /** 姓名 */
    @ApiModelProperty(value = "姓名", position = 6)
    private String name;

    /** 昵称 */
    @ApiModelProperty(value = "昵称", position = 7)
    @TableField(insertStrategy = FieldStrategy.IGNORED, updateStrategy = FieldStrategy.IGNORED)
    private String nickname;

    /** 性别 */
    @ApiModelProperty(value = "性别", position = 8)
    @TableField(insertStrategy = FieldStrategy.IGNORED, updateStrategy = FieldStrategy.IGNORED)
    private String gender;

    /** 分类 */
    @ApiModelProperty(value = "分类", position = 9)
    private String category;

    /** 扩展信息 */
    @ApiModelProperty(value = "扩展信息", position = 10)
    @TableField(insertStrategy = FieldStrategy.IGNORED, updateStrategy = FieldStrategy.IGNORED)
    private String extJson;
}
