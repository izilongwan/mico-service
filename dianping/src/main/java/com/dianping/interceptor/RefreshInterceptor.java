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

import com.common.util.ThreadLocalUtil;
import com.dianping.entity.User;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RefreshInterceptor implements HandlerInterceptor {
    @Resource
    RedisTemplate<String, Object> redisTemplate;

    // add Interceptor 使用new的方式 需要使用构造函数注入redisTemplate
    public RefreshInterceptor(RedisTemplate<String, Object> redisTemplate) {
        if (Objects.isNull(this.redisTemplate)) {
            this.redisTemplate = redisTemplate;
        }
    }

    public RefreshInterceptor() {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        Cookie[] cookies = request.getCookies();

        if (Objects.isNull(redisTemplate)) {
            log.error("redisTemplate = {}", redisTemplate);
            return true;
        }

        if (Objects.isNull(cookies)) {
            return true;
        }

        for (Cookie cookie : cookies) {
            if (Objects.equals(cookie.getName(), "username") && !Objects.equals(cookie.getValue(), null)) {
                String key = "test:user:h" + cookie.getValue();
                Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);

                if (!Objects.isNull(entries)) {
                    redisTemplate.expire(key, 1, TimeUnit.DAYS);
                    User u = BeanUtil.toBean(entries, User.class);
                    ThreadLocalUtil.set("user", u, User.class);
                }
                return true;
            }
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        ThreadLocalUtil.remove("user");
    }
}
