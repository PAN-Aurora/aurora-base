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

    private static final String[] AUTH_WHITELIST = {
            // -- swagger ui
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/webjars/**",
            // swagger-boostrap-ui
            "/doc.html"
    };

    /**
     * 配置自定义拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //针对所有进行token校验拦截
        registry.addInterceptor(
                new JwtTokenInterceptor(redisUtil)).addPathPatterns("/**")

        .excludePathPatterns("/error")
        .excludePathPatterns("/login")
        .excludePathPatterns("/test**/**")

         .excludePathPatterns(AUTH_WHITELIST)
        .excludePathPatterns("/images/**")
        .excludePathPatterns("/configuration/ui");
        //进行api限流拦截
        registry.addInterceptor(new RequestInterceptor(redisUtil)).addPathPatterns("/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 解决静态资源无法访问
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        // 解决swagger-ui.html无法访问
        registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        // 解决swagger的js文件无法访问
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        // 解决swagger-bootstrap-ui文件无法访问
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");

    }
}
