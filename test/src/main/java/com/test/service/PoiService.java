package com.test.service;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;

public interface PoiService {
    ResponseEntity<Object> download(HttpServletResponse response);
}
