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
package com.jackli.dbs.modular.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import com.jackli.common.pojo.CommonEntity;

/**
 * 数据源实体
 *
 * @author lijinchang
 * @date 2022/3/9 9:13
 **/
@Getter
@Setter
@TableName("EXT_DATABASE")
public class DbsStorage extends CommonEntity {

    /** id */
    @ApiModelProperty(value = "id", position = 1)
    private String id;

    /** 租户id */
    @ApiModelProperty(value = "租户id", position = 2)
    private String tenantId;

    /** 名称 */
    @ApiModelProperty(value = "名称", position = 3)
    private String poolName;

    /** 连接URL */
    @ApiModelProperty(value = "连接URL", position = 4)
    private String url;

    /** 用户名 */
    @ApiModelProperty(value = "用户名", position = 5)
    private String username;

    /** 密码 */
    @ApiModelProperty(value = "密码", position = 6)
    private String password;

    /** 驱动名称 */
    @ApiModelProperty(value = "驱动名称", position = 7)
    private String driverName;

    /** 分类 */
    @ApiModelProperty(value = "分类", position = 8)
    private String category;

    /** 排序码 */
    @ApiModelProperty(value = "排序码", position = 9)
    private Integer sortCode;

    /** 扩展信息 */
    @ApiModelProperty(value = "扩展信息", position = 10)
    @TableField(insertStrategy = FieldStrategy.IGNORED, updateStrategy = FieldStrategy.IGNORED)
    private String extJson;
}
