package com.dianping.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("test_u_order")
public class Order implements Serializable {
    String id;
    String orderId;
    String goodsId;
    String userId;
    String createTime;
}
