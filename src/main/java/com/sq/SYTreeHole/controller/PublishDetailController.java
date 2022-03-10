package com.sq.SYTreeHole.controller;

import com.sq.SYTreeHole.common.Constants;
import com.sq.SYTreeHole.common.Result;
import com.sq.SYTreeHole.entity.Publish;
import com.sq.SYTreeHole.exception.PublishDetailException;
import com.sq.SYTreeHole.service.publishService.PublishDetailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.Objects;

@RestController
public class PublishDetailController {

    private final PublishDetailService publishDetailService;

    public PublishDetailController(PublishDetailService publishDetailService) {
        this.publishDetailService = publishDetailService;
    }

    @GetMapping("/publishDetail/{id}")
    public Result<Publish> detail(@PathVariable String id){
        Publish detail = publishDetailService.detail(id);
        if(Objects.isNull(detail))
            throw new PublishDetailException("服务器异常");
        return new Result<>(Constants.CODE_200,"请求成功",detail);
    }
    @GetMapping("/publishDetailDoStar")
    public void doStar(String id){
        publishDetailService.doStar(id);
    }
}
