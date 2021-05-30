package com.mm.common.utils;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 *
 * @author lwl
 */
@Slf4j
@Component
public class RedisUtil {

    public static final Long EXPIRE_TIME = 60 * 60 * 24L;

    public static RedisTemplate redisTemplate;

    public RedisUtil(RedisTemplate redisTemplate) {
        RedisUtil.redisTemplate = redisTemplate;
    }


    /**
     * 批量删除对应的value
     *
     * @param keys
     */
    public static void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 批量删除key
     *
     * @param pattern
     */
    public static void removePattern(final String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * 删除对应的value
     *
     * @param key
     */
    public static boolean remove(String key) {
        boolean result = false;
        if (exists(key) && redisTemplate.delete(key)) {
            result = true;
        }
        return result;
    }

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    public static boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public static String get(final String key) {
        String result = null;
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        if (operations.get(key) != null) {
            result = operations.get(key).toString();
        }
        return result;
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean set(final String key, String value) {
        return set(key, value, EXPIRE_TIME);
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean set(final String key, String value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            log.error("set cache error", e);
        }
        return result;
    }

    public static boolean hashSet(String key, String hk, Object hv) {
        boolean result = false;
        try {
            redisTemplate.opsForHash().put(key, hk, hv);
            result = true;
        } catch (Exception e) {
            log.error("hashSet cache error", e);
        }
        return result;
    }

    public static String hashGet(String key, String hk) {
        String result = null;
        try {
            result = redisTemplate.opsForHash().get(key, hk).toString();
        } catch (Exception e) {
            log.error("hashGet cache error", e);
        }
        return result;
    }

    public static Set<String> hashKeyList(String key) {
        Set<String> result = null;
        try {
            result = redisTemplate.opsForHash().keys(key);
        } catch (Exception e) {
            log.error("hashKeyList cache error", e);
        }
        return result;
    }

    public static void addList(String key, Object obj) {
        redisTemplate.opsForList().rightPush(key, obj);
    }

    public static void addLists(String key, List objs) {
        final RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
        redisTemplate.executePipelined((RedisCallback<Object>) redisConnection -> {
            redisConnection.openPipeline();
            byte[][] bytes = new byte[objs.size()][];
            for (int i = 0; i < objs.size(); i++) {
                bytes[i] = serializer.serialize(JSONUtil.toJsonStr(objs.get(i)));
            }
            redisConnection.lPush(serializer.serialize(key), bytes);
            redisConnection.closePipeline();
            return null;
        });
        log.info("redis addLists is done");
    }

    public static List getListPage(String key, Class clazz, int start, int end) {
        List<Object> result = new ArrayList<>();
        List<String> list = redisTemplate.opsForList().range(key, start, end);
        for (String str : list) {
            Object obj = JSONUtil.toBean(str, clazz);
            result.add(obj);
        }
        return result;
    }

    public static Long getListSize(String key) {
        return redisTemplate.opsForList().size(key);
    }
}
