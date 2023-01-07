package com.dianping.interceptor;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.dianping.entity.User;
import com.dianping.util.ThreadLocalPool;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RefreshInterceptor implements HandlerInterceptor {
    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return true;
        }

        if (redisTemplate == null) {
            log.error("redisTemplate = {}", redisTemplate);
            return true;
        }

        for (Cookie cookie : cookies) {
            if (Objects.equals(cookie.getName(), "username") && !Objects.equals(cookie.getValue(), null)) {
                String key = "test:user:h" + cookie.getValue();
                Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
                ;

                if (!Objects.equals(entries, null)) {
                    redisTemplate.expire(key, 1, TimeUnit.DAYS);
                    User u = BeanUtil.toBean(entries, User.class);
                    ThreadLocalPool.setUser(u);
                }
                return true;
            }
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        ThreadLocalPool.clear();
    }
}
