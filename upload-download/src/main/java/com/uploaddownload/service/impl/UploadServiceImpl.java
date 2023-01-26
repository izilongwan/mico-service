package com.uploaddownload.service.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.uploaddownload.service.UploadService;
import com.uploaddownload.vo.UploadVo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UploadServiceImpl implements UploadService {

    @Value("${upload-config.destPath}")
    String destPath;

    @Value("${upload-config.viewPath}")
    String viewPath;

    String fileSeparator = File.separator;
    String splitor = ".";
    String splitorReg = "\\.";

    @Override
    public UploadVo[] uploadFile(MultipartFile[] files, String dirName) {
        String path = destPath + fileSeparator + dirName + fileSeparator;

        createDirs(path);

        return Stream.of(files).map((file) -> {
            UploadVo uploadVo = new UploadVo();

            try {
                String[] split = file.getOriginalFilename().split(splitorReg);
                String filePath = path + UUID.randomUUID() + splitor + split[split.length - 1];
                File dest = new File(filePath);

                file.transferTo(dest);
                uploadVo.setImg(filePath.replace(destPath, viewPath));
            } catch (Exception e) {
                uploadVo.setCode(-1);
                uploadVo.setMessage(e.getMessage());
            }

            return uploadVo;
        }).toArray(UploadVo[]::new);
    }

    public void createDirs(String destPath) {
        Path path = Paths.get(destPath);

        try {
            Path createDirectories = Files.createDirectories(path);
            log.debug("{}", createDirectories);
        } catch (Exception e) {
            log.error("{}", e);
        }
    }
}
