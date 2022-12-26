package com.test.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.common.aop.anno.RequestLimitAnno;
import com.common.entity.R;
import com.test.dto.DeptDto;
import com.test.entity.Dept;
import com.test.mapper.DeptDtoMapper;
import com.test.mapper.DeptMapper;

@RestController
@RequestMapping("test/dept")
public class DeptController {
    @Resource
    RestTemplate restTemplate;

    @Resource
    DeptDtoMapper deptDtoMapper;

    @Resource
    DeptMapper deptMapper;

    public R getForObject(String url) {
        return restTemplate.getForObject(url, R.class, "");
    }

    @GetMapping("")
    public R<List<DeptDto>> getDept() {
        return R.SUCCESS(deptDtoMapper.select());
    }

    @RequestLimitAnno(value = 3)
    @GetMapping("list")
    public R<List<Dept>> getDeptList() {
        return R.SUCCESS(deptMapper.selectList(null));
    }
}
