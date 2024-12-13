package com.baomidou.dynamic.datasource.annotation;

import java.lang.annotation.*;

/**
 * 主数据源
 *
 * @author lijinchang
 * @date 2022/6/14 18:15
 **/
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@DS("master")
public @interface Master {
}
