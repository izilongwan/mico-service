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

    @GetMapping("{hash}")
    public ResponseEntity<org.springframework.core.io.Resource> download(@PathVariable String hash) {
        return downloadService.download(hash);
    }
}
