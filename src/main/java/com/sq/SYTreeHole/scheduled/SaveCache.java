package com.sq.SYTreeHole.scheduled;

import com.sq.SYTreeHole.Utils.RedisUtils;
import com.sq.SYTreeHole.dao.publishDao.PublishDetailMapper;
import com.sq.SYTreeHole.entity.Publish;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SaveCache {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private PublishDetailMapper publishDetailMapper;

    @Scheduled(cron = "0 7 0 * * *")
    public void savePublishCacheToDataBase() {
        RedisClusterConnection clusterConnection = redisTemplate.getRequiredConnectionFactory().getClusterConnection();
        Map<String, JedisPool> clusterNodes = ((JedisCluster) clusterConnection.getNativeConnection()).getClusterNodes();
        for (Map.Entry<String, JedisPool> JedisPoolEntry : clusterNodes.entrySet()) {
            JedisPool pool = JedisPoolEntry.getValue();
            Jedis jedis = pool.getResource();
            if (jedis.info("replication").contains("role:master")) {
                Set<String> keys = jedis.keys("*");
                for (String key : keys) {
                    Pattern pattern = Pattern.compile("^publish:");
                    Matcher matcher = pattern.matcher(key);
                    if (matcher.find()) {
                        Publish publish = RedisUtils.getPublishCache(key.substring(key.length() - 1));
                        publishDetailMapper.updateById(publish);
                        RedisUtils.delPublishCache(key.substring(key.length() - 1));
                    }
                }
            }
            pool.returnResource(jedis);
        }
        RedisUtils.clearAll();
    }
}