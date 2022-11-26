package com.socket.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.alibaba.fastjson.JSONObject;
import com.common.entity.R;
import com.socket.util.SseUtil;

@RestController
@RequestMapping("sse")
@CrossOrigin
public class SseController {
    @GetMapping("")
    public R<String> home() {
        return R.SUCCESS();
    }

    @PostMapping
    public R<List<String>> post(@RequestBody List<String> ids) {
        return R.SUCCESS(ids);
    }

    @GetMapping(value = "connect/{userId}", produces = { MediaType.TEXT_EVENT_STREAM_VALUE })
    public SseEmitter connect(@PathVariable String userId) {
        return SseUtil.connect(userId);
    }

    @GetMapping("disconnect/{userId}")
    public R<Object> disconnect(@PathVariable String userId) {
        SseEmitter v = SseUtil.removeUser(userId);

        return v == null ? R.ERROR() : R.SUCCESS();
    }

    @PostMapping("send/batch")
    public R<Object> batchSendMessage(@RequestBody Object message) {
        try {
            String jsonString = JSONObject.toJSONString(message);
            ;
            SseUtil.batchSendMessage(jsonString);

            return R.SUCCESS();
        } catch (Exception e) {
            return R.ERROR(e.getMessage());
        }
    }

    @PostMapping("send/person/{userId}")
    public R<Object> sendMessage(@PathVariable String userId, @RequestBody Object message) {
        try {
            String jsonString = JSONObject.toJSONString(message);
            ;
            SseUtil.sendMessage(userId, jsonString);

            return R.SUCCESS();
        } catch (Exception e) {
            return R.ERROR(e.getMessage());
        }
    }

    @PostMapping("send/list/{userIds}")
    public R<Object> listSendMessage(@PathVariable String userIds, @RequestBody Object message) {
        try {
            String[] list = userIds.split("_");
            String jsonString = JSONObject.toJSONString(message);
            ;
            SseUtil.userIdsSendMessage(list, jsonString);

            return R.SUCCESS();
        } catch (Exception e) {
            return R.ERROR(e.getMessage());
        }
    }

    @GetMapping("info/count")
    public R<Integer> getUserCount() {
        int userCount = SseUtil.getUserCount();
        return R.SUCCESS(userCount);
    }

    @GetMapping("info/list")
    public R<ArrayList<String>> getUserIdList() {
        ArrayList<String> userIdList = SseUtil.getUserIdList();

        return R.SUCCESS(userIdList);
    }
}
