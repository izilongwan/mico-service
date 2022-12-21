package com.uploaddownload.controller;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uploaddownload.service.DownloadService;

@RestController
@RequestMapping("download")
public class DownloadController {
    @Resource
    DownloadService downloadService;

    @GetMapping("local/{hash}")
    public ResponseEntity<Object> downloadLocal(@PathVariable String hash) {
        return downloadService.downloadLocal(hash);
    }

    @GetMapping("net/{hash}")
    public ResponseEntity<Object> downloadNet(@PathVariable String hash) {
        return downloadService.downloadNet(hash);
    }
}
