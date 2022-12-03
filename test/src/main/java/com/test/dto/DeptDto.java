package com.test.dto;

import java.util.List;

import com.test.entity.Dept;
import com.test.entity.Emp;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DeptDto extends Dept {
    Integer count;

    String names;

    List<Emp> emps;

    Emp emp;
}
