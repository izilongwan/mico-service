package com.sendmessage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    // 替换序列化方式
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // String的序列化方式
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // 使用GenericJackson2JsonRedisSerializer 替换默认序列化(默认采用的是JDK序列化)
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();

        // key序列化方式采用String类型
        template.setKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson类型
        template.setValueSerializer(genericJackson2JsonRedisSerializer);
        // hash的key序列化方式也是采用String类型
        template.setHashKeySerializer(stringRedisSerializer);
        // hash的value也是采用jackson类型
        template.setHashValueSerializer(genericJackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

}