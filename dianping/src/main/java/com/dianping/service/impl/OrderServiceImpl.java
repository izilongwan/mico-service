package com.dianping.service.impl;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.connection.stream.StreamReadOptions;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.dianping.entity.Goods;
import com.dianping.entity.Order;
import com.dianping.mapper.GoodsMapper;
import com.dianping.mapper.OrderMapper;
import com.dianping.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Resource
    OrderMapper orderMapper;

    @Resource
    GoodsMapper goodsMapper;

    @Resource
    OrderService orderService;

    @Value("${order-kill.streamName}")
    String streamName;

    @Value("${order-kill.groupName}")
    String groupName;

    static final ExecutorService pool = Executors.newSingleThreadExecutor();

    static final DefaultRedisScript<Long> ORDER_SCRIPT = new DefaultRedisScript<>();

    static {
        ORDER_SCRIPT.setLocation(new ClassPathResource("lua/kill-order.lua"));
        ORDER_SCRIPT.setResultType(Long.class);
    }

    // 使用get方式去直接取这个对象来使用，不通过spring获取
    public GoodsMapper getGoodsMapper() {
        return goodsMapper;
    }

    // 使用get方式去直接取这个对象来使用，不通过spring获取
    public OrderMapper getOrderMapper() {
        return orderMapper;
    }

    @PostConstruct
    public void init() {
        try {
            redisTemplate.opsForStream()
                    .createGroup(streamName, "g1");
        } catch (Exception e) {
        }
    }

    public Integer handleOrder1(Goods goods, String uId) {
        String id = goods.getId();

        Goods u = goodsMapper.selectById(id);

        Integer store = u.getStore();

        if (Objects.isNull(u) || store <= 0) {
            return 1;
        }

        synchronized (uId.toString().intern()) {
            // OrderService proxy = ((OrderService) AopContext.currentProxy());
            // return proxy.insertOrder(uId, id, orderMapper, goodsMapper);
            String orderId = genOrderId("test:order|" + id);
            return orderService.insertOrder(uId, id, orderId, orderMapper, goodsMapper);
        }

    }

    public Integer handleOrder2(Goods goods, String uId) {
        String id = goods.getId();

        Goods u = goodsMapper.selectById(id);

        Integer store = u.getStore();

        if (Objects.isNull(u) || store <= 0) {
            return 1;
        }

        String key = "test:order|" + id + ":user|" + uId;

        Boolean setIfAbsent = redisTemplate.opsForValue().setIfAbsent(key, 1, 1, TimeUnit.DAYS);

        if (Boolean.FALSE.equals(setIfAbsent)) {
            return 2;
        }

        String orderId = genOrderId("test:order|" + id);

        orderService.insertOrder(uId, id, orderId, orderMapper, goodsMapper);

        return 0;
    }

    private Runnable handleRunnableOrder(OrderService orderService) {
        final String channelName = "c1";

        return new Runnable() {
            @Override
            public void run() {
                handleOrderRun(1, ">", 1);
            }

            private void handleOrderRun(long seconds, String offset, int type) {
                while (true) {
                    try {
                        StreamReadOptions streamReadOptions = StreamReadOptions.empty().count(1)
                                .block(Duration.ofSeconds(seconds));
                        StreamOffset<String> streamOffset = StreamOffset.create((streamName), ReadOffset.from(offset));
                        List<MapRecord<String, Object, Object>> list = redisTemplate.opsForStream().read(
                                Consumer.from(groupName, channelName),
                                streamReadOptions,
                                streamOffset);

                        if (Objects.isNull(list) || list.isEmpty()) {
                            if (type == 0) {
                                break;
                            }
                            continue;
                        }

                        MapRecord<String, Object, Object> mapRecord = list.get(0);

                        RecordId id = mapRecord.getId();
                        Map<Object, Object> map = mapRecord.getValue();

                        orderService.insertOrder(
                                (String) map.get("user_id"),
                                (String) map.get("goods_id"),
                                (String) map.get("order_id"),
                                orderMapper,
                                goodsMapper);

                        redisTemplate.opsForStream().acknowledge(streamName, groupName, id);

                    } catch (Exception e) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        if (type == 1) {
                            handleOrderRun(1, "0", 0);
                        }
                    }
                }
            }
        };
    }

    public String genOrderId(String orderKey) {
        Long increment = redisTemplate.opsForValue()
                .increment(orderKey + ":" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        return "" + System.currentTimeMillis() + "_" + increment.intValue() + "";
    }

    public Integer handleOrder(Goods goods, String uId) {
        String goodsId = goods.getId();
        String orderKey = "test:order|" + goodsId;
        String userKey = "test:users:" + goodsId;
        String orderId = genOrderId(orderKey);

        Long num = redisTemplate.execute(
                ORDER_SCRIPT,
                Arrays.asList(orderKey, userKey),
                System.currentTimeMillis(),
                uId, goodsId, orderId,
                streamName);

        if (num.intValue() != 0) {
            return num.intValue();
        }

        pool.execute(handleRunnableOrder(orderService));

        return 0;

    }

    @Transactional
    public Integer insertOrder(String userId, String goodsId, String orderId, OrderMapper orderMapper,
            GoodsMapper GoodsMapper) {
        UpdateWrapper<Order> updateWrapperOrder = new UpdateWrapper<Order>();

        // OrderMapper orderMapper = getOrderMapper();

        updateWrapperOrder.lambda().eq(Order::getUserId, userId);

        Integer count = orderMapper.selectCount(updateWrapperOrder);

        if (count > 0) {
            return 1;
        }

        UpdateWrapper<Goods> updateWrapper = new UpdateWrapper<>();

        updateWrapper.lambda()
                .setSql("store = store - 1")
                .gt(Goods::getStore, 0)
                .eq(Goods::getId, Integer.valueOf(goodsId));

        // GoodsMapper goodsMapper = getGoodsMapper();

        int update = GoodsMapper.update(null, updateWrapper);

        if (update == 1) {
            Order order = new Order();
            order.setUserId(userId);
            order.setOrderId(orderId);
            order.setGoodsId(goodsId);

            orderMapper.insert(order);
            return 0;
        }

        return 2;
    }

    public void run() {
        handleRunnableOrder(orderService);
    }
}
