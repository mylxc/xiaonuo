package com.jackli.common.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @description:
 * @author: luoxingcheng
 * @created: 2023/12/19 13:50
 */
public class FmDateTimeUtil {

    /**
     *
     * @param startTIme 开始时间
     * @param endTime 结束时间
     * @return
     */
    public static Long secondsBetween(Date startTIme, Date endTime){
        return  (endTime.getTime() - startTIme.getTime()) / 1000;
    }

    public static Long minuteBetween(Date startTIme, Date endTime){
        return  (endTime.getTime() - startTIme.getTime()) / 1000 * 60;
    }

    /**
     *  加上指定月数
     * @param date
     * @param monthsToAdd
     * @return
     */
    public static Date addMonthsToDate(Date date, long monthsToAdd) {
        Instant instant = date.toInstant();
        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime resultDateTime = localDateTime.plusMonths(monthsToAdd);
        Instant resultInstant = resultDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(resultInstant);
    }

    public static long calculateMinutesDifference(Date specifiedDate) {
        long millisecondsDiff = System.currentTimeMillis() - specifiedDate.getTime();
        return millisecondsDiff / (1000 * 60);
    }

}
