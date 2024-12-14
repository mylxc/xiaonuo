package com.jackli.common.businessEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 1	仓储机械手
 * 2	中央刀库机械手
 * 3	天轨机械手
 */
@Getter
@AllArgsConstructor
public enum EqptSubTypeEnum {

    SILO_IRON_HAND_TYPE(1, "仓储机械手"),
    CENTER_IRON_HAND_TYPE(2, "中央刀库机械手"),
    SKYRAIL_IRON_HAND_TYPE(3, "天轨机械手");

    private Integer code;
    private String descr;

    public static String getEnum(Integer code) {
        for (EqptSubTypeEnum item : EqptSubTypeEnum.values()) {
            if (Objects.equals(code, item.getCode())) {
                return item.descr;
            }
        }
        return null;
    }

}
