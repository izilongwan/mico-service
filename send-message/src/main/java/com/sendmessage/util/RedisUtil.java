package com.sendmessage.util;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;

import com.sendmessage.entity.Mail;

public class RedisUtil {
    public static void setMailValue(RedisTemplate<String, Object> redisTemplate, Mail mail) {
        for (String to : mail.getTo()) {
            redisTemplate.opsForValue()
                    .set(
                            mail.getExpireKey() + to,
                            "1",
                            mail.getExpire(),
                            TimeUnit.SECONDS);
        }
    }

    public static Object getMailValue(RedisTemplate<String, Object> redisTemplate, String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
