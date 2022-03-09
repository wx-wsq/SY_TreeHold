package com.sq.SYTreeHole.service.publishService;

import com.sq.SYTreeHole.entity.Publish;

import java.util.List;

public interface PublishManagementService {

    boolean insert(Publish publish);

    boolean modify(Publish publish);

    boolean delete(Publish publish);

    List<Publish> selectMy(String userId, String page);

}
