package com.m2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.common.entity.R;

@RestController
@RequestMapping("m2")
public class OrderController {
    @GetMapping("")
    public R<String> order() {
        return R.SUCCESS("m2");
    }

    @GetMapping("{id}")
    public R<Integer> id(@PathVariable Integer id) {
        return R.SUCCESS(id);
    }
}
