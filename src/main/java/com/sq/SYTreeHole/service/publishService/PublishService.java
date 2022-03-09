package com.sq.SYTreeHole.service.publishService;

import com.sq.SYTreeHole.entity.Publish;

import java.util.List;

public interface PublishService {

    List<Publish> publishAsAll(Long page,Long count);

//    List<Publish> publishAsHot(Long page,Long count);
}
