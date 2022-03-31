package com.sq.SYTreeHole;

import com.sq.SYTreeHole.Utils.RedisUtils;
import com.sq.SYTreeHole.dao.publishDao.PublishDetailMapper;
import com.sq.SYTreeHole.entity.Publish;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
class SyTreeHoleApplicationTests {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;


    @Test
    void contextLoads() {
        RedisClusterConnection clusterConnection = redisTemplate.getRequiredConnectionFactory().getClusterConnection();
        Map<String, JedisPool> clusterNodes = ((JedisCluster) clusterConnection.getNativeConnection()).getClusterNodes();
        System.out.println(clusterNodes.entrySet());
        for (Map.Entry<String, JedisPool> stringJedisPoolEntry : clusterNodes.entrySet()) {
            Jedis jedis = stringJedisPoolEntry.getValue().getResource();
            if(jedis.info("replication").contains("role:master")){
                Set<String> keys = jedis.keys("*");
                for (String key : keys) {
                    Pattern pattern = Pattern.compile("^publish:");
                    Matcher matcher = pattern.matcher(key);
                    if(matcher.find())
                        System.out.println(key);
                }
            }
        }
    }

}
