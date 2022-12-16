package com.dianping.util;

import java.util.HashMap;
import java.util.Map;

import com.dianping.entity.User;

public class ThreadLocalPool {
    public static ThreadLocal<Map<String, Object>> pool = ThreadLocal.withInitial(HashMap::new);

    public static User getUser() {
        return (User) get("user");
    }

    public static User setUser(Object value) {
        return (User) pool.get().put("user", value);
    }

    public static Object get(String key) {
        return pool.get().get(key);
    }

    public static Object set(String key, Object value) {
        return pool.get().put(key, value);
    }

    public static void clear() {
        pool.get().clear();
    }
}
