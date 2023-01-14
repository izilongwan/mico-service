package com.dianping.service.impl;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.dianping.service.CookieService;

@Service
public class CookieServiceImpl implements CookieService {

    String cookiePrefix = "test:user:cookie:";

    @Resource
    RedisTemplate<String, String> redisTemplate;

    // 服务的响应头中需要携带Access-Control-Allow-Credentials并且为true。
    // 响应头中的Access-Control-Allow-Origin一定不能为*，必须是指定的域名
    // 页面请求时需要携带和设置credentials为true
    @Override
    public String cookieSet(HttpServletRequest request, HttpServletResponse response) {
        String v = System.currentTimeMillis() + "";
        Cookie cookie = new Cookie("username", v);
        String serverName = request.getServerName();

        cookie.setMaxAge(60 * 60 * 24);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setDomain(serverName);
        response.addCookie(cookie);

        String key = cookiePrefix + v;

        HashOperations<String, String, Object> opsForHash = redisTemplate.opsForHash();
        opsForHash.put(key, "name", v);
        opsForHash.put(key, "time", System.currentTimeMillis());
        redisTemplate.expire(key, 1, TimeUnit.DAYS);

        return "1";
    }

    @Override
    public String cookieRm(HttpServletRequest request, HttpServletResponse response, String username) {
        String serverName = request.getServerName();
        Cookie cookie = new Cookie("username", "");

        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setDomain(serverName);
        response.addCookie(cookie);

        String key = cookiePrefix + username;
        redisTemplate.delete(key);
        return "1";
    }

}
