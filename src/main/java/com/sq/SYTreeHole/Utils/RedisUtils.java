package com.sq.SYTreeHole.Utils;

import com.sq.SYTreeHole.entity.Publish;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.util.Date;

/**
 * redis链接
 */
@Component
public class RedisUtils {

    private static RedisTemplate<Object, Object> redisTemplate;

    public RedisUtils(RedisTemplate<Object, Object> redisTemplate) {
        RedisUtils.redisTemplate = redisTemplate;
    }

    public static ValueOperations<Object, Object> getRedisForString() {
        return redisTemplate.opsForValue();
    }

    public static HashOperations<Object, Object, Object> getRedisForHSet() {
        return redisTemplate.opsForHash();
    }

    public static void setPublishesCache(Publish publish) {
        getRedisForHSet().put("publish:" + publish.getId(), "id", publish.getId());
        getRedisForHSet().put("publish:" + publish.getId(), "userId", publish.getUserId());
        getRedisForHSet().put("publish:" + publish.getId(), "title", publish.getTitle());
        getRedisForHSet().put("publish:" + publish.getId(), "text", publish.getText());
        getRedisForHSet().put("publish:" + publish.getId(), "createTime", publish.getCreateTime());
        getRedisForHSet().put("publish:" + publish.getId(), "modifyTime", publish.getModifyTime());
        getRedisForHSet().put("publish:" + publish.getId(), "author", publish.getAuthor());
        getRedisForHSet().put("publish:" + publish.getId(), "mark", publish.getMark());
        getRedisForHSet().put("publish:" + publish.getId(), "visits", publish.getVisits());
        getRedisForHSet().put("publish:" + publish.getId(), "star", publish.getStar());
        getRedisForHSet().put("publish:" + publish.getId(), "commentsNumber", publish.getCommentsNumber());
        getRedisForHSet().put("publish:" + publish.getId(), "version", publish.getVersion());
        getRedisForHSet().put("publish:" + publish.getId(), "isDelete", publish.getIsDelete());

    }

    public static Publish getPublishCache(Serializable id) {
        return new Publish((String) id,
                (String) getRedisForHSet().get("publish:" + id, "userId"),
                (String) getRedisForHSet().get("publish:" + id, "title"),
                (String) getRedisForHSet().get("publish:" + id, "text"),
                (Date) getRedisForHSet().get("publish:" + id, "createTime"),
                (Date) getRedisForHSet().get("publish:" + id, "modifyTime"),
                (String) getRedisForHSet().get("publish:" + id, "author"),
                (Integer) getRedisForHSet().get("publish:" + id, "mark"),
                (Integer) getRedisForHSet().get("publish:" + id, "visits"),
                (Integer) getRedisForHSet().get("publish:" + id, "star"),
                (Integer) getRedisForHSet().get("publish:" + id, "commentsNumber"),
                (Integer) getRedisForHSet().get("publish:" + id, "version"),
                (Integer) getRedisForHSet().get("publish:" + id, "isDelete"));

    }

}
