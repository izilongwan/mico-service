package com.dianping.service;

import com.dianping.entity.Goods;
import com.dianping.mapper.GoodsMapper;
import com.dianping.mapper.OrderMapper;

public interface OrderService {
    Integer handleOrder(Goods goods, String uId);

    Integer handleOrder1(Goods goods, String uId);

    Integer handleOrder2(Goods goods, String uId);

    Integer insertOrder(String string, String string2, String string3, OrderMapper orderMapper,
            GoodsMapper goodsMapper);

    void run();
}
