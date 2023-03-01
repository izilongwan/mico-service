package com.test.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "部门实体")
public class Dept implements Serializable {
    public Integer id;

    @ApiModelProperty(value = "部门名称")
    public String name;

    @TableField(fill = FieldFill.INSERT)
    public String createTime;

    // @TableField(update = "now()")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    public String updateTime;
}
