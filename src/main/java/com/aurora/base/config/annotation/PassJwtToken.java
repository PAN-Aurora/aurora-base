package com.aurora.base.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用来是否校验过滤token
 * @author PHQ
 * @create 2020-05-02 20:23
 **/
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PassJwtToken {
    //是否必须
    boolean required() default true;
}
