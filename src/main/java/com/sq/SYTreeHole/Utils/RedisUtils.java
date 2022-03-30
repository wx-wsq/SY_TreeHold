package com.sq.SYTreeHole.Utils;

import com.sq.SYTreeHole.entity.Publish;
import com.sq.SYTreeHole.entity.PublishImages;
import com.sq.SYTreeHole.entity.User;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class RedisUtils {

    private static RedisTemplate<String, Object> redisTemplate;

    public RedisUtils(RedisTemplate<String, Object> redisTemplate) {
        RedisUtils.redisTemplate = redisTemplate;
    }

    public static ValueOperations<String, Object> getRedisForObject() {
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
        getRedisForHash().put("publish:" + publish.getId(), "user", publish.getUser());
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
                (User) getRedisForHash().get("publish:" + id, "user"),
                (Integer) getRedisForHash().get("publish:" + id, "version"),
                (Integer) getRedisForHash().get("publish:" + id, "isDelete"));

    }

    public static void setPublishListCacheOfId(String type,Serializable page, String ids) {
        getRedisForHash().put(type, "publishPage:" + page, ids);
    }

    public static void clearPublishListCacheOfId(String type){
        redisTemplate
                .getRequiredConnectionFactory()
                .getClusterConnection()
                .del(type.getBytes(StandardCharsets.UTF_8));
    }

    public static void clearAll(){
        redisTemplate.getRequiredConnectionFactory().getClusterConnection().flushAll();
    }

    public static void delPublishCache(Serializable publishId){
        redisTemplate.delete("publish:"+publishId);
    }

    public static void setPublishListCache(List<Publish> publishes){
        publishes.forEach(RedisUtils::setPublishCache);
    }

    public static List<Publish> getPublishListCache(String type,Serializable page) {
        List<Publish> list = new ArrayList<>();
        String hotString = ((String) getRedisForHash().get(type, "publishPage:" + page));
        if(Strings.isBlank(hotString))
            return list;
        String[] hots = hotString.split(",");
        for (String hot : hots) {
            list.add(getPublishCache(hot));
        }
        return list;
    }

    public static void incrPublishVisits(Publish publish) {
        getRedisForHash().put("publish:" + publish.getId(), "visits", publish.getVisits() + 1);
    }

    public static void publishStar(Publish publish,Integer IOrD) {
        getRedisForHash().put("publish:" + publish.getId(), "star", publish.getStar()+IOrD);
    }

    public static void setPublishImagesCache(List<PublishImages> images){
        if(images.size()>0)
            getRedisForObject().set("imageForPublishId:"+images.get(0).getPublishId(),images);
    }
    @SuppressWarnings("all")
    public static List<PublishImages> getPublishImageCache(String publishId){
        return (List<PublishImages>)getRedisForObject().get("imageForPublishId:"+publishId);
    }

    public static void delPublishImageCache(String publishId){
        redisTemplate
                .getRequiredConnectionFactory()
                .getClusterConnection()
                .del(publishId.getBytes(StandardCharsets.UTF_8));
    }


}
