package com.dianping.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.common.entity.R;
import com.dianping.entity.Goods;
import com.dianping.service.CookieService;
import com.dianping.service.OrderService;

@RestController
@RequestMapping("dianping/user")
@CrossOrigin(value = { "http://localhost:9999" }, allowCredentials = "true")
public class UserController {
    @Resource
    CookieService cookieService;

    @Resource
    OrderService orderService;

    @GetMapping("cookie/set")
    @Transactional
    public R<String> cookieSet(HttpServletRequest request, HttpServletResponse response) {
        return R.SUCCESS(cookieService.cookieSet(request, response));
    }

    @GetMapping("cookie/rm")
    @Transactional
    public R<String> cookieRm(HttpServletRequest request, HttpServletResponse response,
            @CookieValue(name = "username") String username) {
        return R.SUCCESS(cookieService.cookieRm(request, response, username));
    }

    @GetMapping("res")
    public R<String> res(@CookieValue("username") String username) {
        return R.SUCCESS(username);
    }

    @PostMapping("test/comsume2/{u_id}")
    public R<Object> handleOrder1(@RequestBody Goods goods, @PathVariable("u_id") String uId) {
        Integer num = orderService.handleOrder1(goods, uId);
        return R.AUTO(num);
    }

    @PostMapping("test/comsume3/{u_id}")
    public R<Object> handleOrder2(@RequestBody Goods goods, @PathVariable("u_id") String uId) {
        Integer num = orderService.handleOrder2(goods, uId);
        return R.AUTO(num);

    }

    @PostMapping("test/comsume/{u_id}")
    public R<Object> handleOrder(@RequestBody Goods goods, @PathVariable("u_id") String uId) {
        Integer num = orderService.handleOrder(goods, uId);
        return R.AUTO(num);
    }

}
