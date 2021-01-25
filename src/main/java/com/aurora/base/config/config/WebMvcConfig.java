package com.aurora.base.config.config;

import com.aurora.base.common.util.RedisUtil;
import com.aurora.base.config.interceptor.JwtTokenInterceptor;
import com.aurora.base.config.interceptor.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/**
 * webmvc 配置
 * @author PHQ
 * @create 2020-05-05 20:36
 **/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 配置自定义拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //针对所有进行token校验拦截
        registry.addInterceptor(new JwtTokenInterceptor(redisUtil)).addPathPatterns("/**")
        .excludePathPatterns("/error")
        .excludePathPatterns("/static/**")
        .excludePathPatterns("/swagger-ui.html")
        .excludePathPatterns("/swagger-resources/**")
        .excludePathPatterns("/images/**")
        .excludePathPatterns("/webjars/**")
        .excludePathPatterns("/v2/api-docs")
        .excludePathPatterns("/configuration/ui")
        .excludePathPatterns("/login")
        .excludePathPatterns("/test**/**");
        //进行api限流拦截
        registry.addInterceptor(new RequestInterceptor(redisUtil)).addPathPatterns("/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    }
}
