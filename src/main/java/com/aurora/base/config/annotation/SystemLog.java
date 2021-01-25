package com.aurora.base.config.annotation;

import java.lang.annotation.*;

/**
 * 日志注解
 *    记录行为日志
 * @author PHQ
 * @create 2020-05-01 16:59
 **/
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemLog {

    String module() default ""; // 模块说明,例如：系统管理-角色管理－列表页面

    String methods() default ""; // 方法说明，例如：角色管理

    String desc() default ""; // 方法描述

    String url() default  ""; //请求的url
}
