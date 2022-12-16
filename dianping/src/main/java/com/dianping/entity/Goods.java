package com.dianping.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("test_goods")
public class Goods implements Serializable {
    String id;
    String name;
    Integer store;
    String createTime;
}
