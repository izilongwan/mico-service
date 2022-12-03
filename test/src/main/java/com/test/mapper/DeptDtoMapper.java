package com.test.mapper;

import java.util.List;

import com.test.dto.DeptDto;

public interface DeptDtoMapper {
    List<DeptDto> selectNames();

    List<DeptDto> select();

    DeptDto selectOne(DeptDto deptDto);

    DeptDto selectList(DeptDto deptDto);
}
