package com.uploaddownload.service;

import org.springframework.http.ResponseEntity;

public interface DownloadService {
    ResponseEntity<Object> downloadLocal(String hash);

    ResponseEntity<Object> downloadNet(String hash);
}
