package com.jackli.common.util;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: 时间处理
 * @author: luoxingcheng
 * @created: 2023/12/29 10:18
 */
@Slf4j
public class TimeConverterUtil {
    public static Integer convertTimeToSeconds(String timeStr) {
        if(StringUtils.isBlank(timeStr)){
            return 0;
        }
        try {
            String[] parts = timeStr.split(":");
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            int seconds = Integer.parseInt(parts[2]);

            return hours * 3600 + minutes * 60 + seconds;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            log.error("时间转换异常,time:{},异常:{}", timeStr, e);
            return 0;
        }
    }

    public static Integer convertTimeToMinutes(String timeStr) {
        Integer seconds = convertTimeToSeconds(timeStr);
        return seconds / 60;
    }
}
