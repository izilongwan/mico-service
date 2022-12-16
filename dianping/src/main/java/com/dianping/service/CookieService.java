package com.dianping.service;

import javax.servlet.http.HttpServletResponse;

public interface CookieService {
    String cookieSet(HttpServletResponse response);

    String cookieRm(HttpServletResponse response,
            String username);
}
