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
package com.jackli.sys.modular.org.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fhs.core.trans.vo.TransPojo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import com.jackli.common.pojo.CommonEntity;

/**
 * 组织实体
 *
 * @author lijinchang
 * @date 2022/4/21 16:13
 **/
@Getter
@Setter
@TableName("SYS_ORG")
public class SysOrg extends CommonEntity implements TransPojo {

    /** id */
    @ApiModelProperty(value = "id", position = 1)
    private String id;

    /** 租户id */
    @ApiModelProperty(value = "租户id", position = 2)
    private String tenantId;

    /** 父id */
    @ApiModelProperty(value = "父id", position = 3)
    private String parentId;

    /** 主管id */
    @ApiModelProperty(value = "主管id", position = 4)
    @TableField(insertStrategy = FieldStrategy.IGNORED, updateStrategy = FieldStrategy.IGNORED)
    private String directorId;

    /** 名称 */
    @ApiModelProperty(value = "名称", position = 5)
    private String name;

    /** 编码 */
    @ApiModelProperty(value = "编码", position = 6)
    private String code;

    /** 分类 */
    @ApiModelProperty(value = "分类", position = 7)
    private String category;

    /** 排序码 */
    @ApiModelProperty(value = "排序码", position = 8)
    private Integer sortCode;

    /** 扩展信息 */
    @ApiModelProperty(value = "扩展信息", position = 9)
    @TableField(insertStrategy = FieldStrategy.IGNORED, updateStrategy = FieldStrategy.IGNORED)
    private String extJson;
}
