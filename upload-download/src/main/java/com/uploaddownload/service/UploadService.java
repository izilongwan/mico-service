package com.uploaddownload.service;

import org.springframework.web.multipart.MultipartFile;

import com.uploaddownload.vo.UploadVo;

public interface UploadService {
    UploadVo[] uploadFile(MultipartFile[] files, String dirName);
}
