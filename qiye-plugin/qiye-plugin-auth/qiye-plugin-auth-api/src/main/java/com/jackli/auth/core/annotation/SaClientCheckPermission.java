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
package com.jackli.auth.core.annotation;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import org.springframework.core.annotation.AliasFor;
import com.jackli.auth.core.util.StpClientUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限认证(前台User版)：必须具有指定权限才能进入该方法
 * 可标注在函数、类上（效果等同于标注在此类的所有方法上）
 *
 * @author lijinchang
 * @date 2022/3/10 10:40
 **/
@SaCheckPermission(type = StpClientUtil.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE})
public @interface SaClientCheckPermission {

    /**
     * 需要校验的权限码
     * @return 需要校验的权限码
     */
    @AliasFor(annotation = SaCheckPermission.class)
    String [] value() default {};

    /**
     * 验证模式：AND | OR，默认AND
     * @return 验证模式
     */
    @AliasFor(annotation = SaCheckPermission.class)
    SaMode mode() default SaMode.AND;

}