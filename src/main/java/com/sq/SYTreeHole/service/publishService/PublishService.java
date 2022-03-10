package com.sq.SYTreeHole.service.publishService;

import com.sq.SYTreeHole.entity.Publish;

import java.util.List;

public interface PublishService {

    /**
     * 按时间排序查找
     * @param page 页数，默认一次请求返回十条
     * @return 返回对象集合
     */
    List<Publish> publishAsAll(String page);

    /**
     * 按热度排序查找
     * @param page 页数，默认一次请求返回十条
     * @return 返回对象集合
     */
    List<Publish> publishAsHot(String page);
}
