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
package com.jackli.ten.core.prop;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 多租户相关配置
 *
 * @author lijinchang
 * @date 2022/1/2 17:03
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "snowy.config.ten")
public class TenProperties {

    /** 是否开启多租户 */
    private Boolean enabled;

    /** 租户id隔离的租户模式下，需要忽略拼接租户id字段的表名称，逗号分割 */
    private String ignoreTableNames;

    /** 租户id隔离的租户模式下，租户字段名称（数据表需具备对应字段名称） */
    private String tenIdColumnName;

    /** 租户id隔离的租户模式下，默认租户ID */
    private String defaultTenId;

}
