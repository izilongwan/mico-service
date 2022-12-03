package com.test.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class Emp implements Serializable {
    public Integer id;
    public String name;
    public Integer genderId;
    public Integer deptId;
    public Double salary;
    public String createTime;

    public String deptName;
}
