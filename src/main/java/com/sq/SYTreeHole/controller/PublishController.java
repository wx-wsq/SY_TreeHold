package com.sq.SYTreeHole.controller;


import com.sq.SYTreeHole.common.Constants;
import com.sq.SYTreeHole.common.Result;
import com.sq.SYTreeHole.entity.Publish;
import com.sq.SYTreeHole.service.publishService.PublishService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PublishController {

    private final PublishService publishService;

    public PublishController(PublishService publishService) {
        this.publishService = publishService;
    }

    @GetMapping("/publishAll")
    public Result<?> publishesAsAll(String start){
        List<Publish>  list = publishService.publishAsAll(start);

        return new Result<>(Constants.CODE_200,"请求成功",list);
    }

    @GetMapping("/publishHot")
    public Result<?> publishesAsHot(String start){
        List<Publish> list = publishService.publishAsHot(start);

        return new Result<>(Constants.CODE_200,"请求成功",list);
    }
}
