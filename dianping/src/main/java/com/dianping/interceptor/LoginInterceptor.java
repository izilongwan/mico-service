package com.dianping.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.alibaba.fastjson.JSON;
import com.common.entity.R;
import com.dianping.entity.User;
import com.dianping.util.ThreadLocalPool;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        User u = ThreadLocalPool.getUser();

        if (u == null) {
            setErrorResponse(response);
            return false;
        }

        return true;
    }

    void setErrorResponse(HttpServletResponse response) throws IOException {
        response.setStatus(401);
        response.setContentType("application/json;charset=UTF-8");

        PrintWriter writer = response.getWriter();
        R<Object> error = R.ERROR("未登录");

        writer.println(JSON.toJSONString(error));
    }
}
