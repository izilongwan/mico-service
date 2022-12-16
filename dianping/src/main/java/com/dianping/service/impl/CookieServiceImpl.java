package com.dianping.service.impl;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.dianping.service.CookieService;

@Service
public class CookieServiceImpl implements CookieService {

    @Resource
    RedisTemplate<String, String> redisTemplate;

    @Override
    public String cookieSet(HttpServletResponse response) {
        String v = System.currentTimeMillis() + "";
        Cookie cookie = new Cookie("username", v);
        cookie.setMaxAge(60 * 60 * 24);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setDomain("localhost");
        response.addCookie(cookie);

        String key = "test:user:h" + v;

        redisTemplate.opsForHash().put(key, "name", "lee");
        redisTemplate.opsForHash().put(key, "time", System.currentTimeMillis());
        redisTemplate.expire(key, 1, TimeUnit.DAYS);

        return "0";
    }

    @Override
    public String cookieRm(HttpServletResponse response, String username) {
        Cookie cookie = new Cookie("username", "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setDomain("localhost");
        response.addCookie(cookie);

        String key = "test:user:h" + username;
        redisTemplate.delete(key);
        return "1";
    }

}
