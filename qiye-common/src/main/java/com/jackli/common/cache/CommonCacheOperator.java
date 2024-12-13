/*
 * Copyright [2022] [https://www.jcxxdd.com]
 *
 * Jackli采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Jackli源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.jcxxdd.com
 * 5.不可二次分发开源参与同类竞品，如有想法可联系团队767076381@qq.com商议合作。
 * 6.若您的项目无法满足以上几点，需要更多功能代码，获取Jackli商业授权许可，请在官网购买授权，地址为 https://www.jcxxdd.com
 */
package com.jackli.common.cache;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 通用Redis缓存操作器
 *
 * @author lijinchang
 * @date 2022/6/21 16:00
 **/
@Component
public class CommonCacheOperator {


    /** 所有缓存Key的前缀 */
    private static final String CACHE_KEY_PREFIX = "Cache:";

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public void put(String key, Object value) {
        redisTemplate.boundValueOps(CACHE_KEY_PREFIX + key).set(value);
    }

    public void put(String key, Object value, long timeoutSeconds) {
        redisTemplate.boundValueOps(CACHE_KEY_PREFIX + key).set(value, timeoutSeconds, TimeUnit.SECONDS);
    }

    public Object get(String key) {
        return redisTemplate.boundValueOps(CACHE_KEY_PREFIX + key).get();
    }

    public Object getValue(String key) {
        return redisTemplate.boundValueOps(key).get();
    }

    public void remove(String... key) {
        ArrayList<String> keys = CollectionUtil.toList(key);
        List<String> withPrefixKeys = keys.stream().map(i -> CACHE_KEY_PREFIX + i).collect(Collectors.toList());
        redisTemplate.delete(withPrefixKeys);
    }

    public Collection<String> getAllKeys() {
        Set<String> keys = redisTemplate.keys(CACHE_KEY_PREFIX + "*");
        if (keys != null) {
            // 去掉缓存key的common prefix前缀
            return keys.stream().map(key -> StrUtil.removePrefix(key, CACHE_KEY_PREFIX)).collect(Collectors.toSet());
        } else {
            return CollectionUtil.newHashSet();
        }
    }

    public Collection<Object> getAllValues() {
        Set<String> keys = redisTemplate.keys(CACHE_KEY_PREFIX + "*");
        if (keys != null) {
            return redisTemplate.opsForValue().multiGet(keys);
        } else {
            return CollectionUtil.newArrayList();
        }
    }

    public Map<String, Object> getAllKeyValues() {
        Collection<String> allKeys = this.getAllKeys();
        HashMap<String, Object> results = MapUtil.newHashMap();
        for (String key : allKeys) {
            results.put(key, this.get(key));
        }
        return results;
    }

    /**
     * 获取redis总指定hash对象
     *
     * @param key
     * @return
     */
    public Map<Object, Object> getHashFromRedis(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     *  获取指定hash对象中指定的field的值
     * @param key
     * @param field
     * @return
     */
    public Object getHashFieldValue(String key, Object field) {
        Map<Object, Object> hash = redisTemplate.opsForHash().entries(key);
        return hash.get(field);
    }

    public Long getAndIncrement(String key) {
        ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();
        // 使用incr命令实现自增，如果key不存在，则会先将key的值初始化为0再进行自增
        return valueOps.increment(key, 1L);
    }

    public Long getCurrentValue(String key) {
        ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();
        // 获取当前值，如果key不存在，则返回null
        return (Long)valueOps.get(key);
    }

    public Long incrementAndGet(String key) {
        ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();
        // 先自增1，再获取自增后的值
        return valueOps.increment(key, 1L);
    }

    public List<String> getList(String key) {
        List<Object> range = redisTemplate.opsForList().range(key, 0, -1);
        return convertObjectListToStringList(range);
    }

    public void putList(String key, List<String> values,long ttlSeconds) {
        values.forEach(value -> {
            redisTemplate.opsForList().leftPush(key, value);
        });
        redisTemplate.expire(key, ttlSeconds, TimeUnit.SECONDS);
    }

    private List<String> convertObjectListToStringList(List<Object> objectList) {
        List<String> stringList = new ArrayList<>();
        for (Object obj : objectList) {
            stringList.add(obj.toString());
        }
        return stringList;
    }

    public void deleteKey(String key) {
        redisTemplate.delete(CACHE_KEY_PREFIX + key);
    }

}
