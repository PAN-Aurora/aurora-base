package com.aurora.base.config.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 *  限流注解
 * @author :PHQ
 * @date：2020/5/14
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface GuavaRateLimiter {

    //以固定数值往令牌桶添加令牌 1
    double permitsPerSecond() default 1;

    //获取令牌最大等待时间 1000 毫秒
    long timeout() default 1000;

    // 单位(例:分钟/秒/毫秒) 默认:毫秒
    TimeUnit timeunit() default TimeUnit.MILLISECONDS;

    // 无法获取令牌返回提示信息 默认值可以自行修改
    String msg() default   "系统繁忙,请稍后再试！";

}
