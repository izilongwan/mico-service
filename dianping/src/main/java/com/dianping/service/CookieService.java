package com.dianping.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CookieService {
    String cookieSet(HttpServletRequest request, HttpServletResponse response);

    String cookieRm(HttpServletRequest request, HttpServletResponse response,
            String username);
}
