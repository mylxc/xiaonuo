package com.baomidou.dynamic.datasource.annotation;


import java.lang.annotation.*;

/**
 * 数据源切换注解
 *
 * @author lijinchang
 * @date 2022/6/14 18:14
 **/
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DS {

    /**
     * 组名称或特定的数据库名称或spring SPEL名称。
     *
     * @author lijinchang
     * @date 2022/6/14 18:15
     **/
    String value();
}
