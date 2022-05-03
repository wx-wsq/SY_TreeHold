package com.sq.SYTreeHole.service.publishService;

import com.sq.SYTreeHole.entity.Notice;

import java.util.List;

public interface NoticeService {

    /**
     * 查询自己的通知
     * @param userId 用户id
     * @return 返回通知集合
     */
    List<Notice> findMyNotices(String userId);


    /**
     * 设置通知
     * @param notice 通知实体
     */
    void setMyNotice(Notice notice);
}
