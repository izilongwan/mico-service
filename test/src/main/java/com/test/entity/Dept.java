package com.test.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.common.aop.anno.NotNullAnno;
import com.common.aop.anno.ValidatorAnno;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dept implements Serializable {
    @ValidatorAnno("[1-9]")
    public Integer id;

    @NotNullAnno
    public String name;

    @TableField(fill = FieldFill.INSERT)
    public String createTime;

    // @TableField(update = "now()")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    public String updateTime;
}
