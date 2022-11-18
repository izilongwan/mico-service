package com.socket.util;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SseUtil {
    private static AtomicInteger count = new AtomicInteger(0);

    private static Map<String, SseEmitter> map = new ConcurrentHashMap<>();

    public static SseEmitter connect(String userId) {
        if (map.containsKey(userId)) {
            return map.get(userId);
        }

        try {
            SseEmitter sseEmitter = new SseEmitter(0L);

            sseEmitter.onCompletion(onCompletionCallBack(userId));
            sseEmitter.onError(onErrorCallBack(userId));
            sseEmitter.onTimeout(onTimeoutCallBack(userId));

            count.getAndIncrement();

            map.put(userId, sseEmitter);

            return sseEmitter;
        } catch (Exception e) {
            log.error("用户[{}]连接异常: {}", userId, e.getMessage());
        }
        return null;
    }

    public static void sendMessage(String userId, String message) {
        if (map.containsKey(userId)) {
            sendMessage(userId, map.get(userId), message);
        }
    }

    public static void sendMessage(String userId, SseEmitter sseEmitter, String message) {
        try {
            sseEmitter.send(message, MediaType.APPLICATION_JSON);
        } catch (Exception e) {
            log.error("用户[{}]推送异常:{}", userId, e.getMessage());
            removeUser(userId);
        }
    }

    public static void groundSendMessage(String groundId, String message) {
        if (map.size() > 0) {
            map.forEach((k, v) -> {
                if (k.startsWith(groundId)) {
                    sendMessage(k, v, message);
                }
            });
        }
    }

    public static void batchSendMessage(String message) {
        if (map.size() > 0) {
            map.forEach((k, v) -> sendMessage(k, v, message));
        }
    }

    /**
     * @param array
     * @param message
     */
    public static void userIdsSendMessage(String[] userIds, String message) {
        for (String userId : userIds) {
            sendMessage(userId, message);
        }
    }

    public static int getUserCount() {
        return count.intValue();
    }

    public static ArrayList<String> getUserIdList() {
        return new ArrayList<>(map.keySet());
    }

    public static SseEmitter removeUser(String userId) {
        SseEmitter v = map.remove(userId);

        if (v != null) {
            count.getAndDecrement();
            log.info("移除用户：{}", userId);
        }

        return v;
    }

    public static Runnable onCompletionCallBack(String userId) {
        return () -> {
            removeUser(userId);
            log.info("结束连接：{}", userId);
        };
    }

    public static Consumer<Throwable> onErrorCallBack(String userId) {
        return throwable -> {
            removeUser(userId);
            log.info("连接超时：{}", userId);
        };
    }

    public static Runnable onTimeoutCallBack(String userId) {
        return () -> {
            removeUser(userId);
            log.info("连接异常：{}", userId);
        };
    }
}
