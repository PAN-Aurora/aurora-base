package com.aurora.base.config.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 *JedisConfig 配置
 *   加载项目下 redis.properties
 * @author PHQ
 * @create 2020-05-08 21:53
 **/
@Component
@Configuration
public class JedisConfig {

    @Value("${spring.redis.host}")
    public String host;
    @Value("${spring.redis.port}")
    public int port;
    @Value("${spring.redis.database}")
    public int database;
    @Value("${spring.redis.jedis.pool.max-idle}")
    public int maxIdle;
    @Value("${spring.redis.jedis.pool.min-idle}")
    public int minIdle;
    @Value("${spring.redis.jedis.pool.max-active}")
    public int maxActive;
    @Value("${spring.redis.jedis.pool.max-wait}")
    public long maxWait;
    @Value("${spring.redis.timeout}")
    public long timeout;
}
