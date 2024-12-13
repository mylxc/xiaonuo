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
package com.jackli.dbs.modular.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 数据源编辑参数
 *
 * @author lijinchang
 * @date 2022/7/29 9:59
 */
@Getter
@Setter
public class DbsStorageEditParam {

    /** id */
    @ApiModelProperty(value = "id", required = true, position = 1)
    @NotBlank(message = "id不能为空")
    private String id;

    /** 连接URL */
    @ApiModelProperty(value = "连接URL", required = true, position = 3)
    @NotBlank(message = "url不能为空")
    private String url;

    /** 用户名 */
    @ApiModelProperty(value = "用户名", required = true, position = 4)
    @NotBlank(message = "username不能为空")
    private String username;

    /** 密码 */
    @ApiModelProperty(value = "密码", required = true, position = 5)
    @NotBlank(message = "password不能为空")
    private String password;

    /** 排序码 */
    @ApiModelProperty(value = "排序码", required = true, position = 6)
    @NotNull(message = "sortCode不能为空")
    private Integer sortCode;

    /** 扩展信息 */
    @ApiModelProperty(value = "扩展信息", position = 7)
    private String extJson;
}
