package com.mm.common.util;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Redis工具类
 *
 * @author lwl
 */
@Slf4j
@Component
public class RedisUtil {

    public static RedisTemplate redisTemplate;

    public RedisUtil(RedisTemplate redisTemplate) {
        RedisUtil.redisTemplate = redisTemplate;
    }

    /**
     * 默认过期时间一天
     */
    public static final Long DEFAULT_EXPIRE = 86400L;

    /**
     * 批量删除对应的value
     *
     * @param keys
     */
    public static void remove(String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 批量删除key
     *
     * @param pattern
     */
    public static void removePattern(String pattern) {
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
        return redisTemplate.delete(key);
    }

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    public static boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public static Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean set(final String key, Object value) {
        return set(key, value, DEFAULT_EXPIRE);
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean set(final String key, Object value, Long expireTime) {
        try {
            redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(expireTime));
            return true;
        } catch (Exception e) {
            log.error("set cache error", e);
        }
        return false;
    }

    public static boolean hashSet(String key, String hk, Object hv) {
        try {
            redisTemplate.opsForHash().put(key, hk, hv);
            return true;
        } catch (Exception e) {
            log.error("hashSet cache error", e);
        }
        return false;
    }

    public static Object hashGet(String key, String hk) {
        return redisTemplate.opsForHash().get(key, hk);
    }

    public static Set hashKeyList(String key) {
        return redisTemplate.opsForHash().keys(key);
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
