package com.test.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.service.PoiService;

@RestController
@RequestMapping("test/poi")
public class PoiController {
    @Resource
    PoiService poiService;

    @GetMapping("download")
    public ResponseEntity<Object> donwload(HttpServletResponse response) {
        return poiService.download(response);
    }
}
