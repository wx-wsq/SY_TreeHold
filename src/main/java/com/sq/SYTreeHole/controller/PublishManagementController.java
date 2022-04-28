package com.sq.SYTreeHole.controller;

import com.sq.SYTreeHole.DTO.PublishDTO;
import com.sq.SYTreeHole.common.Constants;
import com.sq.SYTreeHole.common.Result;
import com.sq.SYTreeHole.entity.Publish;
import com.sq.SYTreeHole.entity.PublishImages;
import com.sq.SYTreeHole.exception.ManagementPublishException;
import com.sq.SYTreeHole.service.publishService.PublishManagementService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PublishManagementController {

    private final PublishManagementService publishManagementService;

    public PublishManagementController(PublishManagementService publishManagementService) {
        this.publishManagementService = publishManagementService;
    }

    @PostMapping("/publishInsert")
    public Result<?> insert(Publish publish, HttpServletRequest httpServletRequest) {
        boolean isInsert = publishManagementService.insert(publish, httpServletRequest);
        if (isInsert)
            return new Result<>(Constants.CODE_200, "添加成功！", null);
        else
            throw new ManagementPublishException("服务器异常，插入失败");
    }

    @PostMapping("/publishModify")
    public Result<?> modify(Publish publish, HttpServletRequest httpServletRequest) {
        boolean isModify = publishManagementService.modify(publish, httpServletRequest);
        if (isModify)
            return new Result<>(Constants.CODE_200, "修改成功！", null);
        else
            throw new ManagementPublishException("服务器异常，修改失败");
    }

    @PostMapping("/publishDel")
    public Result<?> delete(String publishId, String userId) {
        boolean isDelete = publishManagementService.delete(publishId, userId);
        if (isDelete)
            return new Result<>(Constants.CODE_200, "删除成功", null);
        else
            throw new ManagementPublishException("服务器异常删除失败");
    }

    @PostMapping("publishGet")
    public Result<?> selectMy(String userId, String page, String index) {
        List<Publish> publishList = publishManagementService.selectMyPublish(userId, page, index);
        if (publishList.size() == 0)
            return new Result<>(Constants.CODE_200, "无数据", null);
        List<List<PublishImages>> imageList = publishManagementService.selectMyPublishImage(publishList);
        return new Result<>(Constants.CODE_200, "成功", getPublishDTOs(publishList, imageList));
    }

    private List<PublishDTO> getPublishDTOs(List<Publish> publishList, List<List<PublishImages>> publishImages) {
        List<PublishDTO> publishDTOS = new ArrayList<>();
        for (Publish publish : publishList) {
            int flag = 0;
            for (List<PublishImages> publishImage : publishImages) {
                if (publishImage.size() != 0 && publish.getId().equals(publishImage.get(0).getPublishId())) {
                    List<String> urls = publishImage
                            .stream()
                            .map(PublishImages::getUrl)
                            .collect(Collectors.toList());
                    publishDTOS.add(new PublishDTO(publish, urls));
                    flag = 1;
                    break;
                }
            }
            if (flag == 0)
                publishDTOS.add(new PublishDTO(publish, null));
        }
        return publishDTOS;
    }
}
