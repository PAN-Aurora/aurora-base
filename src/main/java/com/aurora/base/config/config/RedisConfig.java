package com.aurora.base.config.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

/**
 * redis 配置
 * @author PHQ
 * @create 2020-05-08 21:33
 **/
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    private final static Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    @Autowired
    JedisConfig jedisConfig;
    @Autowired
    JedisConnectionFactory jedisConnectionFactory;

    /**
     * 构建jedis连接池工厂
     * @return
     */
    @Bean
    public JedisConnectionFactory jedisConnectionFactory (){
        logger.info("############构建JedisConnectionFactory#############");

        //创建redis配置对象 配置地址 端口 密码
        RedisStandaloneConfiguration redis=new RedisStandaloneConfiguration();
        redis.setDatabase(jedisConfig.database);
        redis.setHostName(jedisConfig.host);
        redis.setPort(jedisConfig.port);

        //设置JedisClientConfiguration 连接超时时间
        JedisClientConfiguration.JedisClientConfigurationBuilder jedisClientConfiguration = JedisClientConfiguration.builder();
        jedisClientConfiguration.connectTimeout(Duration.ofMillis(jedisConfig.timeout));

        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder client=
                (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder)JedisClientConfiguration.builder();
        //创建jedis连接池 配置连接池相关属性
        JedisPoolConfig jedisPoolConfig=new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(jedisConfig.maxIdle);
        jedisPoolConfig.setMinIdle(jedisConfig.minIdle);
        jedisPoolConfig.setMaxTotal(jedisConfig.maxActive);
        jedisPoolConfig.setMaxWaitMillis(jedisConfig.maxWait);

        //创建jedis客户端配置
        client.poolConfig(jedisPoolConfig);
        //构建jedis连接工厂
        JedisConnectionFactory jedisConnectionFactory=new JedisConnectionFactory(redis,client.build());
        return jedisConnectionFactory;
    }

    /**
     * 构建 RedisTemplate
     * @return
     */
    @Bean
    public RedisTemplate redisTemplate(){
        logger.info("############构建RedisTemplate#############");

        //创建redis序列化对象Jackson2JsonRedisSerializer
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer=new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        //构建RedisTemplate
        RedisTemplate redisTemplate=new RedisTemplate();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);

        //设置redis key 和value 序列化方式
        RedisSerializer rs=new StringRedisSerializer();
        //string
        redisTemplate.setKeySerializer(rs);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        //hash
        redisTemplate.setHashKeySerializer(rs);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }
}
