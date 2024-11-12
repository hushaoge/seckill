package com.solar.seckill.cache;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hushaoge
 * @date 2024/11/12 8:26
 * @description
 */
@Component
public class RedisService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public void setKeyValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public Object getValueByKey(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     *
     * @param luaScript lua脚本
     * @param keys      键
     * @param args      参数
     * @return
     */
    public Long execScript(String luaScript, List<String> keys, Object... args) {
        return redisTemplate.execute(RedisScript.of(luaScript, Long.class), keys, args);
    }
}
