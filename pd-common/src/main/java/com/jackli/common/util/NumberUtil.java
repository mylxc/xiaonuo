package com.jackli.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * @description: 数字处理
 * @author: luoxingcheng
 * @created: 2023/10/05 15:52
 */
public class NumberUtil {

    /**
     *
     * @param fnumber 浮点数
     * @return
     */
    public static String decimalPlaces(float fnumber){
        DecimalFormat decimalFormat = new DecimalFormat("0%");
        return decimalFormat.format(fnumber);
    }

    public static String mathCeil(float fnumber){
        int result = (int)Math.floor(fnumber);
        return String.valueOf(result);
    }

    /**
     *
     * @param v1
     * @param v2
     * @return
     */
    public static String div(float v1, float v2){
        BigDecimal dividendNumber = new BigDecimal(v1);
        BigDecimal divisorNumber = new BigDecimal(v2);
        BigDecimal discussNumber = dividendNumber.divide(divisorNumber, 2, BigDecimal.ROUND_FLOOR);

        DecimalFormat decimalFormat = new DecimalFormat("0");
        return decimalFormat.format(discussNumber);
    }

    /**
     *  相除
     * @param v1
     * @param v2
     * @return
     */
    public static String IntegerDiv(Integer v1, Integer v2){
        BigDecimal dividendBigDecimal = new BigDecimal(v1);
        BigDecimal divisorBigDecimal = new BigDecimal(v2);
        BigDecimal result = dividendBigDecimal.divide(divisorBigDecimal, 2, RoundingMode.HALF_UP);
        return result.toString();
    }

    /**
     * 判断字符串是否是正整数
     * @param str
     * @return
     */
    public static boolean isPositiveInteger(String str) {
        return str.matches("\\d+") && !str.startsWith("0");
    }
}
