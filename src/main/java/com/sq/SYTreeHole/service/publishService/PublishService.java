package com.sq.SYTreeHole.service.publishService;

import com.sq.SYTreeHole.entity.Publish;

import java.util.List;

public interface PublishService {

    List<Publish> publishAsAll(String page);

    List<Publish> publishAsHot(String page);
}
