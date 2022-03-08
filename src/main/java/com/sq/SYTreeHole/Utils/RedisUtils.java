package com.sq.SYTreeHole.Utils;

import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * redis链接
 */
@Component
public class RedisUtils {

    private static RedisTemplate<Object,Object> redisTemplate;

    public RedisUtils(RedisTemplate<Object, Object> redisTemplate) {
        RedisUtils.redisTemplate = redisTemplate;
    }

    public static RedisClusterConnection getRedis(){
        return redisTemplate.getRequiredConnectionFactory().getClusterConnection();
    }
}
