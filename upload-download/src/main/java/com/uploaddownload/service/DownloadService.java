package com.uploaddownload.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface DownloadService {
    ResponseEntity<Resource> download(String hash);
}
