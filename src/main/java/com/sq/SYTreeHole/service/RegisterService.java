package com.sq.SYTreeHole.service;

import com.sq.SYTreeHole.entity.User;

public interface RegisterService {

    /**
     *
     * @param user Controller传来的User对象
     * @return 是否添加成功
     */
    boolean register(User user);
}
