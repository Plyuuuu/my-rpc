package github.veikkoroc.factory;

import java.util.HashMap;
import java.util.Map;

/**
 * 获取代理对象的工厂
 * @author Veikko Roc
 * @version 1.0
 * @date 2021/4/5 16:13
 */
public class SingletonFactory {
    /**
     *  本地缓存
     */
    private static final Map<String, Object> OBJECT_MAP = new HashMap<>();

    private SingletonFactory() {
    }

    public static <T> T getInstance(Class<T> c) {
        String key = c.toString();
        Object instance = OBJECT_MAP.get(key);
        synchronized (c) {
            if (instance == null) {
                try {
                    instance = c.newInstance();
                    OBJECT_MAP.put(key, instance);
                } catch (IllegalAccessException | InstantiationException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
        }
        return c.cast(instance);
    }
}
