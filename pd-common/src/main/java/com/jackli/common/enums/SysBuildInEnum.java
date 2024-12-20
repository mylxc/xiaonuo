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
package com.jackli.common.enums;

import lombok.Getter;

/**
 * 系统内置的不可删除的标识枚举
 *
 * @author lijinchang
 * @date 2022/4/21 19:56
 **/
@Getter
public enum SysBuildInEnum {

    /** 超管用户账号 */
    BUILD_IN_USER_ACCOUNT("superAdmin", "超管"),

    /** 超管角色编码 */
    BUILD_IN_ROLE_CODE("superAdmin", "超级管理员"),

    /** 系统内置模块编码 */
    BUILD_IN_MODULE_CODE("system", "系统内置"),

    /** 系统内置单页面编码 */
    BUILD_IN_SPA_CODE("system", "系统内置"),

    /** 系统内置非租户菜单编码 */
    BUILD_IN_NO_TEN_MENU_CODE("noten", "非租户菜单");

    private final String value;

    private final String name;

    SysBuildInEnum(String value, String name) {
        this.value = value;
        this.name = name;
    }
}
