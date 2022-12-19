package com.uploaddownload.service.impl;

import java.io.File;
import java.io.FileInputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.uploaddownload.service.DownloadService;

@Service
public class DownloadImpl implements DownloadService {
    @Value("${upload-config.destPath}")
    String destPath;

    /**
     * @param hash
     * @return
     */
    @Override
    public ResponseEntity<Resource> download(String hash) {
        String replaceHash = hash.replaceAll("@", File.separator);
        HttpHeaders headers = new HttpHeaders();

        try {

            File file = new File(destPath + File.separator + replaceHash);
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + file.getName() + "\"");
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

            headers.addAll(headers);

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "not exist;attachment;filename=\"" +
                    replaceHash + "\"");
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(null);
        }
    }
}
