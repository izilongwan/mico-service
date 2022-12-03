package com.test.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class Emp implements Serializable {
    Integer id;
    String name;
    Integer genderId;
    Integer deptId;
    Double salary;
    String createTime;

    String deptName;
}
