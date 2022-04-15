package com.sq.SYTreeHole.Utils;

import com.sq.SYTreeHole.entity.Publish;
import com.sq.SYTreeHole.entity.PublishImages;
import com.sq.SYTreeHole.entity.User;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
@Data
@ConfigurationProperties(prefix = "my-redis-cache-config")
public class RedisUtils {

    private static boolean enable;

    public static boolean isEnable() {
        return enable;
    }

    public static void setEnable(boolean enable) {
        RedisUtils.enable = enable;
    }

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
        if(!enable)
            return;
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
        if(!enable)
            return new Publish();
        return new Publish(
                (String) getRedisForHash().get("publish:" + id, "id"),
                (String) getRedisForHash().get("publish:" + id, "userId"),
                (String) getRedisForHash().get("publish:" + id, "title"),
                (String) getRedisForHash().get("publish:" + id, "text"),
                (Date) getRedisForHash().get("publish:" + id, "createTime"),
                (Date) getRedisForHash().get("publish:" + id, "modifyTime"),
                (String) getRedisForHash().get("publish:" + id, "author"),
                (Double) getRedisForHash().get("publish:" + id, "mark"),
                (Integer) getRedisForHash().get("publish:" + id, "visits"),
                (Integer) getRedisForHash().get("publish:" + id, "star"),
                (Integer) getRedisForHash().get("publish:" + id, "commentsNumber"),
                (User) getRedisForHash().get("publish:" + id, "user"),
                (Integer) getRedisForHash().get("publish:" + id, "version"),
                (Integer) getRedisForHash().get("publish:" + id, "isDelete"));

    }

    public static void setPublishListCacheOfId(String type,Serializable page, String ids, String index) {
        if(!enable)
            return;
        getRedisForHash().put(type, "publishPage:" + page+",index:"+index, ids);
    }

    public static void clearPublishListCacheOfId(String type){
        if(!enable)
            return;
        redisTemplate
                .getRequiredConnectionFactory()
                .getClusterConnection()
                .del(type.getBytes(StandardCharsets.UTF_8));
    }

    public static void clearAll(){
        if(!enable)
            return;
        redisTemplate.getRequiredConnectionFactory().getClusterConnection().flushAll();
    }

    public static void delPublishCache(Serializable publishId){
        if(!enable)
            return;
        redisTemplate.delete("publish:"+publishId);
    }

    public static void setPublishListCache(List<Publish> publishes){
        if(!enable)
            return;
        publishes.forEach(RedisUtils::setPublishCache);
    }

    public static List<Publish> getPublishListCache(String type,Serializable page) {
        List<Publish> list = new ArrayList<>();
        if(!enable)
            return list;
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
        if(!enable)
            return;
        getRedisForHash().put("publish:" + publish.getId(), "visits", publish.getVisits() + 1);
    }

    public static void publishStar(Publish publish,Integer IOrD) {
        if(!enable)
            return;
        getRedisForHash().put("publish:" + publish.getId(), "star", publish.getStar()+IOrD);
    }

    public static void setPublishImagesCache(List<PublishImages> images){
        if(!enable)
            return;
        if(images.size()>0)
            getRedisForObject().set("imageForPublishId:"+images.get(0).getPublishId(),images);
    }
    @SuppressWarnings("all")
    public static List<PublishImages> getPublishImageCache(String publishId){
        if(!enable)
            return null;
        return (List<PublishImages>)getRedisForObject().get("imageForPublishId:"+publishId);
    }

    public static void delPublishImageCache(String publishId){
        if(!enable)
            return;
        redisTemplate
                .getRequiredConnectionFactory()
                .getClusterConnection()
                .del(publishId.getBytes(StandardCharsets.UTF_8));
    }


}
