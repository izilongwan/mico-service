package com.uploaddownload.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.common.entity.R;
import com.uploaddownload.service.UploadService;
import com.uploaddownload.vo.UploadVo;

@RestController
@RequestMapping("upload")
public class UploadController {
    @Resource
    UploadService uploadService;

    // POST属性名必须为files，或者指定
    @PostMapping("")
    public R<UploadVo[]> uploadFile(@RequestPart MultipartFile[] files) {
        String dirName = "common";
        UploadVo[] uploadFile = uploadService.uploadFile(files, dirName);

        return R.SUCCESS(uploadFile);
    }

    @PostMapping("{dirName}")
    public R<UploadVo[]> uploadFileDirname(@RequestPart MultipartFile[] files, @PathVariable String dirName) {
        UploadVo[] uploadFile = uploadService.uploadFile(files, dirName);

        return R.SUCCESS(uploadFile);
    }
}
