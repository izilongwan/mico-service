package com.test.dto;

import java.util.List;

import com.test.entity.Dept;
import com.test.entity.Emp;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DeptDto extends Dept {

    public DeptDto() {

    }

    public Integer count;

    public String names;

    public List<Emp> emps;

    public Emp emp;

    // 手动重写继承的属性
    @Override
    public String toString() {
        return "DeptDto [id=" + id + ", name=" + name + ", createTime=" + createTime + ", count=" + count + ", names="
                + names + ", emps=" + emps + ", emp=" + emp + "]";
    }

}
