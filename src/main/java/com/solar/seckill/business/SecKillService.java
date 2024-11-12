package com.solar.seckill.business;

import com.google.common.collect.Lists;
import com.solar.seckill.cache.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hushaoge
 * @date 2024/11/12 8:44
 * @description
 */
@Slf4j
@Service
public class SecKillService {

    @Resource
    private RedisService redisService;

    /**
     * 初始化库存
     * @param productId   产品ID
     * @param stock       库存数量
     */
    public void initStock(String productId, int stock) {
        String stockKey = "product_stock:" + productId;
        redisService.setKeyValue(stockKey, stock);
    }

    /**
     * 秒杀
     * @param productId  产品ID
     * @param userId     用户ID
     * @return
     */
    public boolean secKill(String productId, String userId) {
        String luaScript = "local stock_key = KEYS[1];"
                + "local user_key = KEYS[2];"
                + "local stock = tonumber(redis.call('get', stock_key));"
                + "if stock <= 0 then return -1 end;"
                + "if redis.call('sismember', user_key, ARGV[1]) == 1 then return 0 end;"
                + "redis.call('decr', stock_key);"
                + "redis.call('sadd', user_key, ARGV[1]);"
                + "return 1;";
        String stockKey = "product_stock:" + productId;
        String userKey = "user_seckill:" + productId;

        Object result = redisService.execScript(luaScript, Lists.<String>newArrayList(stockKey, userKey), userId);
        if (result.equals(1L)) {
            log.info("秒杀成功");
            // TODO do something
            // 可以把秒杀成功的用户信息，商品放入MQ，异步执行。防止并发操作数据库，减少数据库压力
        } else if(result.equals(0L)) {
            log.warn("您已经秒杀过了！");
            return false;
        } else  {
            log.warn("库存不足！");
            return false;
        }
        return true;
    }
}
