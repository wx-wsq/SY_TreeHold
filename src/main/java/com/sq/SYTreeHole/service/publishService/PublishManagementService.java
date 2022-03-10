package com.sq.SYTreeHole.service.publishService;

import com.sq.SYTreeHole.entity.Publish;

import java.util.List;

public interface PublishManagementService {

    Publish insert(Publish publish);

    Publish modify(Publish publish);

    Publish delete(Publish publish);

    List<Publish> selectMy(String userId, String page);

}
