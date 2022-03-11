package com.sq.SYTreeHole.Utils;

import com.sq.SYTreeHole.entity.Publish;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class RedisUtils {

    private static RedisTemplate<String, String> redisTemplate;

    public RedisUtils(RedisTemplate<String, String> redisTemplate) {
        RedisUtils.redisTemplate = redisTemplate;
    }

    public static ValueOperations<String, String> getRedisForString() {
        return redisTemplate.opsForValue();
    }

    public static HashOperations<String, String, Object> getRedisForHash() {
        return redisTemplate.opsForHash();
    }

    public static void setPublishCache(Publish publish) {
        getRedisForHash().put("publish:" + publish.getId(), "id", publish.getId());
        getRedisForHash().put("publish:" + publish.getId(), "userId", publish.getUserId());
        getRedisForHash().put("publish:" + publish.getId(), "title", publish.getTitle());
        getRedisForHash().put("publish:" + publish.getId(), "text", publish.getText());
        getRedisForHash().put("publish:" + publish.getId(), "createTime", publish.getCreateTime());
        getRedisForHash().put("publish:" + publish.getId(), "modifyTime", publish.getModifyTime());
        getRedisForHash().put("publish:" + publish.getId(), "author", publish.getAuthor());
        getRedisForHash().put("publish:" + publish.getId(), "mark", publish.getMark());
        getRedisForHash().put("publish:" + publish.getId(), "visits", publish.getVisits());
        getRedisForHash().put("publish:" + publish.getId(), "star", publish.getStar());
        getRedisForHash().put("publish:" + publish.getId(), "commentsNumber", publish.getCommentsNumber());
        getRedisForHash().put("publish:" + publish.getId(), "version", publish.getVersion());
        getRedisForHash().put("publish:" + publish.getId(), "isDelete", publish.getIsDelete());

    }

    public static Publish getPublishCache(Serializable id) {
        return new Publish((String) id,
                (String) getRedisForHash().get("publish:" + id, "userId"),
                (String) getRedisForHash().get("publish:" + id, "title"),
                (String) getRedisForHash().get("publish:" + id, "text"),
                (Date) getRedisForHash().get("publish:" + id, "createTime"),
                (Date) getRedisForHash().get("publish:" + id, "modifyTime"),
                (String) getRedisForHash().get("publish:" + id, "author"),
                (Integer) getRedisForHash().get("publish:" + id, "mark"),
                (Integer) getRedisForHash().get("publish:" + id, "visits"),
                (Integer) getRedisForHash().get("publish:" + id, "star"),
                (Integer) getRedisForHash().get("publish:" + id, "commentsNumber"),
                (Integer) getRedisForHash().get("publish:" + id, "version"),
                (Integer) getRedisForHash().get("publish:" + id, "isDelete"));

    }

    public static void setPublishListCacheOfId(String type,Serializable page, String ids) {
        getRedisForHash().put(type, "page:" + page, ids);
    }

    public static void setPublishListCache(List<Publish> publishes){
        for (Publish publish : publishes)
            setPublishCache(publish);
    }
    public static List<Publish> getPublishListCache(String type,Serializable page) {
        List<Publish> list = new ArrayList<>();
        String hotString = ((String) getRedisForHash().get(type, "page:" + page));
        if(Strings.isBlank(hotString))
            return list;
        String[] hots = hotString.split(",");
        for (String hot : hots) {
            list.add(getPublishCache(hot));
        }
        return list;
    }

    public static void incrVisits(Publish publish) {
        getRedisForHash().put("publish:" + publish.getId(), "visits", publish.getVisits() + 1);
    }

    public static void incrStar(Publish publish) {
        getRedisForHash().put("publish:" + publish.getId(), "star", publish.getStar() + 1);
    }

}
