package com.sq.SYTreeHole.service.publishService;

import com.sq.SYTreeHole.entity.Publish;
import java.io.Serializable;

public interface PublishDetailService {

    /**
     * 发表的详情页（每访问一次，浏览次数增加）
     * @param publishId 发表ID
     * @return 发表实体
     */
    Publish detail(Serializable publishId);


    /**
     * 点赞
     * @param publishId 发表ID
     */
    void star(Serializable publishId,Integer IOrD);
}
