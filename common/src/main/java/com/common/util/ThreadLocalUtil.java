package com.common.util;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class ThreadLocalUtil {
    public static ThreadLocal<Map<String, Object>> pool = ThreadLocal.withInitial(HashMap::new);

    public static Object get(String key) {
        return getMap().get(key);
    }

    public static <T> T get(String key, Class<T> clazz) {
        return (T) getMap().get(key);
    }

    public static Object set(String key, Object value) {
        return getMap().put(key, value);
    }

    public static <T> T set(String key, T value, Class<T> clazz) {
        return (T) getMap().put(key, value);
    }

    public static Map<String, Object> getMap() {
        return pool.get();
    }

    public static void clear() {
        getMap().clear();
    }

    public static Object remove(String key) {
        return getMap().remove(key);
    }
}
