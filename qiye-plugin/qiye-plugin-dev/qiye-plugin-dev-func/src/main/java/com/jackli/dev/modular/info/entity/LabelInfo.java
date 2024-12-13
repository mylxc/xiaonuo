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
package com.jackli.dev.modular.info.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * 标签信息实体
 *
 * @author 李金昌
 * @date  2023/08/17 13:43
 **/
@Getter
@Setter
@TableName("LABEL_INFO")
public class LabelInfo {

    /** ID */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "ID", position = 1)
    private Long id;

    /** 标签名 */
    @ApiModelProperty(value = "标签名", position = 2)
    private String labelName;

    /** 创建人姓名 */
    @ApiModelProperty(value = "创建人姓名", position = 3)
    private String createName;

    /** 修改者姓名 */
    @ApiModelProperty(value = "修改者姓名", position = 4)
    private String updateName;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间", position = 5)
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /** 创建用户 */
    @ApiModelProperty(value = "创建用户", position = 6)
    @TableField(fill = FieldFill.INSERT)
    private String createUser;

    /** 修改时间 */
    @ApiModelProperty(value = "修改时间", position = 7)
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    /** 修改用户 */
    @ApiModelProperty(value = "修改用户", position = 8)
    @TableField(fill = FieldFill.UPDATE)
    private String updateUser;

    /** 删除标志 */
    @ApiModelProperty(value = "删除标志", position = 9)
    @TableLogic
    private Integer deleteFlag;

    /** 语言设置列表 */
    @ApiModelProperty(value = "语言设置列表", position = 10)
    @TableField(exist = false)
    private List<LabelValue> valueList;
}
