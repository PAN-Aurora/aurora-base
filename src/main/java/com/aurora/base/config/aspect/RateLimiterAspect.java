package com.aurora.base.config.aspect;

import com.alibaba.druid.util.StringUtils;
import com.aurora.base.common.model.ResponseModel;
import com.aurora.base.common.model.ResultCode;
import com.aurora.base.config.annotation.GuavaRateLimiter;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 限流拦截器
 *
 * @author :PHQ
 * @date：2020/5/14
 **/
@Configuration
@Aspect
public class RateLimiterAspect {

    private final static Logger logger = LoggerFactory.getLogger(RateLimiterAspect.class);

    /**
     * 使用url做为key,存放令牌桶 防止每次重新创建令牌桶
     */
    private Map<String, RateLimiter> limitMap = Maps.newConcurrentMap();

    @Pointcut("@annotation(com.aurora.base.config.annotation.GuavaRateLimiter)")
    public void guavaRateLimiter() {
    }

    @Around("guavaRateLimiter()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取request,response
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();

        // 获取请求url(存在map集合的key)
        String url = request.getRequestURI();
        // 获取自定义注解
        GuavaRateLimiter rateLimiter = getGuavaRateLimiter(joinPoint);
        if (rateLimiter != null) {
            RateLimiter limiter = null;
            // 判断map集合中是否有创建有创建好的令牌桶
            if (!limitMap.containsKey(url)) {
                // 创建令牌桶
                limiter = RateLimiter.create(rateLimiter.permitsPerSecond());
                //存放到map中
                limitMap.put(url, limiter);
                logger.info("请求URL:{},创建令牌桶,容量{} 成功!!!", url, rateLimiter.permitsPerSecond());
            }
            limiter = limitMap.get(url);
            // 获取令牌
            boolean acquire = limiter.tryAcquire(rateLimiter.timeout(), rateLimiter.timeunit());
            //获取不到令牌
            if (!acquire) {
                logger.error(rateLimiter.msg());
                ResponseModel.responseResult(response, ResultCode.LIMITER_SERVER_ERROR.getCode(),rateLimiter.msg());
                return null;
            }
        }
        return joinPoint.proceed();
    }

    /**
     * 获取注解对象
     * @param joinPoint 对象
     * @return ten LogAnnotation
     */
    private GuavaRateLimiter getGuavaRateLimiter(final JoinPoint joinPoint) {
        Method[] methods = joinPoint.getTarget().getClass().getDeclaredMethods();
        String name = joinPoint.getSignature().getName();
        if (!StringUtils.isEmpty(name)) {
            for (Method method : methods) {
                GuavaRateLimiter annotation = method.getAnnotation(GuavaRateLimiter.class);
                if (annotation !=null  && name.equals(method.getName())) {
                    return annotation;
                }
            }
        }
        return null;
    }
}