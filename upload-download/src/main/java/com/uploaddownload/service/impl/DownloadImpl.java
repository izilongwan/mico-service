package com.uploaddownload.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.common.entity.R;
import com.uploaddownload.service.DownloadService;

@Service
public class DownloadImpl implements DownloadService {
    @Value("${upload-config.destPath}")
    String destPath;

    @Value("${upload-config.viewPath}")
    String viewPath;

    /**
     * @param hash
     * @return
     */
    @Override
    public ResponseEntity<Object> downloadLocal(String hash) {
        String replaceHash = hash.replaceAll("@", File.separator);

        try {

            File file = new File(destPath + File.separator + replaceHash);
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            HttpHeaders headers = buildDownloadHeaders(file.getName());

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.ok()
                    .body(R.ERROR(e, e.getClass().toString()));
        }
    }

    public ResponseEntity<Object> downloadNet(String hash) {
        String replaceHash = hash.replaceAll("@", File.separator);
        String targetPath = viewPath + File.separator + replaceHash;

        try {
            URL url = new URL(targetPath);

            URLConnection connection = url.openConnection();
            InputStream openStream = connection.getInputStream();
            InputStreamResource resource = new InputStreamResource(openStream);

            String[] split = replaceHash.split(File.separator);

            HttpHeaders headers = buildDownloadHeaders(split[split.length - 1]);

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(connection.getContentLength())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.ok()
                    .body(R.ERROR(e, e.getClass().toString()));
        }
    }

    private HttpHeaders buildDownloadHeaders(String filename) {
        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + filename + "\"");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

        headers.addAll(headers);

        return headers;
    }
}
