package com.paladin.framework.service.impl;

import com.paladin.framework.service.DataVersionDAO;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 基于redis实现分布式下数据容器的同步
 *
 * @author TontoZhou
 * @since 2020/10/21
 */
public class RedisDataVersionDao implements DataVersionDAO {

    private String keyPrefix = "_DCVersion_";
    private RedisTemplate<String, String> redisTemplate;


    public RedisDataVersionDao(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public RedisDataVersionDao(String keyPrefix, RedisTemplate<String, String> redisTemplate) {
        this.keyPrefix = keyPrefix;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public long getDataVersion(String containerId) {
        String version = redisTemplate.opsForValue().get(keyPrefix + containerId);
        return version == null ? 0 : Long.valueOf(version);
    }

    @Override
    public void incrementVersion(String containerId) {
        redisTemplate.opsForValue().increment(keyPrefix + containerId);
    }


    @Override
    public void setDataVersion(String containerId, long version) {
        redisTemplate.opsForValue().set(keyPrefix + containerId, String.valueOf(version));
    }


}
