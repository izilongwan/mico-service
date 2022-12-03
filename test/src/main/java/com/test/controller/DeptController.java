package com.test.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.common.entity.R;
import com.test.dto.DeptDto;
import com.test.mapper.DeptDtoMapper;

@RestController
@RequestMapping("test/dept")
public class DeptController {
    @Resource
    RestTemplate restTemplate;

    @Resource
    DeptDtoMapper deptDtoMapper;

    public R getForObject(String url) {
        return restTemplate.getForObject(url, R.class, "");
    }

    @GetMapping("")
    public R<List<DeptDto>> getDept() {
        return R.SUCCESS(deptDtoMapper.select());
    }
}
