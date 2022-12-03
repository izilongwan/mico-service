package com.test.mapper;

import java.util.List;

import com.test.entity.Emp;

public interface EmpMapper {
    int add(Emp emp);

    int delete(int id);

    int update(Emp emp);

    List<Emp> selectByDept(Emp emp);
}
