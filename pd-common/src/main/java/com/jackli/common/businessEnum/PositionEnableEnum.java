package com.jackli.common.businessEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 1：启动；0：停用
 */
@Getter
@AllArgsConstructor
public enum PositionEnableEnum {

    PRODUCT(0, "停用"),
    BLANK(1, "启动");

    private Integer code;
    private String descr;

    public static String getEnum(Integer code) {
        for (PositionEnableEnum item : PositionEnableEnum.values()) {
            if (Objects.equals(code, item.code)) {
                return item.descr;
            }
        }
        return null;
    }
}
