package com.sq.SYTreeHole.controller;


import com.sq.SYTreeHole.common.Constants;
import com.sq.SYTreeHole.common.Result;
import com.sq.SYTreeHole.entity.Publish;
import com.sq.SYTreeHole.exception.ManagementPublishException;
import com.sq.SYTreeHole.service.publishService.PublishManagementService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class ManagementPublishController {

    private final PublishManagementService publishManagementService;

    public ManagementPublishController(PublishManagementService publishManagementService) {
        this.publishManagementService = publishManagementService;
    }

    @PostMapping("/publishInsert")
    public Result<?> insert(Publish publish) {
        boolean isInsert = publishManagementService.insert(publish);
        if (isInsert)
            return new Result<>(Constants.CODE_200, "添加成功！", null);
        else
            throw new ManagementPublishException("服务器异常，插入失败");
    }

    @PostMapping("/publishModify")
    public Result<?> modify(Publish publish) {
        boolean isModify = publishManagementService.modify(publish);

        if (isModify)
            return new Result<>(Constants.CODE_200, "修改成功！", null);
        else
            throw new ManagementPublishException("服务器异常，修改失败");
    }

    @PostMapping("/publishDel")
    public Result<?> delete(String publishId,String userId) {
        boolean isDelete = publishManagementService.delete(publishId,userId);
        if (isDelete)
            return new Result<>(Constants.CODE_200, "删除成功", null);
        else
            throw new ManagementPublishException("服务器异常删除失败");
    }

    @PostMapping("publishGet")
    public Result<?> selectMy(String userId, String page) {
        List<Publish> list = publishManagementService.selectMy(userId, page);
        if (list.size() == 0)
            return new Result<>(Constants.CODE_200, "无数据", null);
        else
            return new Result<>(Constants.CODE_200, "成功", list);
    }
}
