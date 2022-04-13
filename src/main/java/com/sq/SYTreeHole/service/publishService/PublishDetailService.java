package com.sq.SYTreeHole.service.publishService;

import com.sq.SYTreeHole.entity.Publish;
import com.sq.SYTreeHole.entity.PublishImages;
import java.io.Serializable;
import java.util.List;

public interface PublishDetailService {

    /**
     * 发表的详情页（每访问一次，浏览次数增加）
     * @param publishId 发表ID
     * @return 发表实体
     */
    Publish detail(String publishId);

    /**
     * 获取发表详情页中的图片
     * @param publishId 发表ID
     * @return 图片列表
     */
    List<PublishImages> publishImages(String publishId);

    /**
     * 点赞(取消赞)
     * @param publishId 发表ID
     */
    void star(Serializable publishId,Integer IOrD);
}
