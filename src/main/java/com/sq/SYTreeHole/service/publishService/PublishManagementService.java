package com.sq.SYTreeHole.service.publishService;

import com.sq.SYTreeHole.entity.Publish;

import java.io.Serializable;
import java.util.List;

public interface PublishManagementService {

    boolean insert(Publish publish);

    boolean modify(Publish publish);

    boolean delete(String publishId, String userId);

    List<Publish> selectMy(String userId, String page);

}
