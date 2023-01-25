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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.aop.anno.LogAnno;
import com.common.aop.anno.RequestLimitAnno;
import com.common.aop.anno.ValidatorAnno;
import com.common.entity.R;
import com.test.dto.DeptDto;
import com.test.entity.Dept;
import com.test.mapper.DeptDtoMapper;
import com.test.mapper.DeptMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("test/dept")
@RequestLimitAnno
@Api(value = "test/dept", description = "controller for test/dept")
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

    @ValidatorAnno
    @GetMapping("")
    @ApiOperation(value = "", notes = "获取全部dept")
    public R<List<DeptDto>> getDept() {
        return R.SUCCESS(deptDtoMapper.select());
    }

    @RequestLimitAnno(value = 3)
    @GetMapping("list")
    public R<List<Dept>> getDeptList() {
        return R.SUCCESS(deptMapper.selectList(null));
    }

    @GetMapping("list/{current}/{pageSize}")
    @ApiOperation(value = "", notes = "获取分页dept")
    public R<Page<Dept>> getDeptListPage(
            @ApiParam(value = "当前分页", required = true) @ValidatorAnno(value = "[1-9]\\d?") @PathVariable Integer current,
            @ApiParam(value = "分页数量", required = true) @ValidatorAnno(pattern = "[1-9]\\d?") @PathVariable Integer pageSize) {
        Page<Dept> page = new Page<>(current, pageSize);

        return R.SUCCESS(deptMapper.selectPage(page, null));
    }

    @GetMapping("{id}")
    public R<Dept> getDeptById(@PathVariable @ValidatorAnno("\\d") Long id) {
        return R.SUCCESS(deptMapper.selectById(id));
    }

    @LogAnno
    @LogAnno("oOo")
    @PostMapping("")
    public R<Dept> getDeptById2(@RequestBody Dept dept) {
        return R.SUCCESS(deptMapper.selectById(dept.getId()));
    }
}
