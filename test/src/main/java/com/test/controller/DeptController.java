package com.test.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.common.aop.anno.LogAnno;
import com.common.aop.anno.RangeValidatorAnno;
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

    @RangeValidatorAnno
    @GetMapping("")
    public R<List<DeptDto>> getDept() {
        return R.SUCCESS(deptDtoMapper.select());
    }

    @RequestLimitAnno(value = 3)
    @GetMapping("list")
    public R<List<Dept>> getDeptList() {
        return R.SUCCESS(deptMapper.selectList(null));
    }

    @GetMapping("{id}")
    public R<Dept> getDeptById(@PathVariable @RangeValidatorAnno(max = 10) Long id) {
        return R.SUCCESS(deptMapper.selectById(id));
    }

    @LogAnno
    @LogAnno("oOo")
    @PostMapping("")
    public R<Dept> getDeptById2(@RequestBody Dept dept) {
        return R.SUCCESS(deptMapper.selectById(dept.getId()));
    }
}
