package com.sq.SYTreeHole.controller;


import com.sq.SYTreeHole.DTO.PublishDTO;
import com.sq.SYTreeHole.common.Constants;
import com.sq.SYTreeHole.common.Result;
import com.sq.SYTreeHole.entity.Publish;
import com.sq.SYTreeHole.entity.PublishImages;
import com.sq.SYTreeHole.service.publishService.PublishService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PublishController {

    private final PublishService publishService;

    public PublishController(PublishService publishService) {
        this.publishService = publishService;
    }

    @GetMapping("/publishAll")
    public Result<?> publishesAsAll(String page, String index) {
        List<Publish> publishList = publishService.publishAsAll(page, index);
        if (publishList.size() == 0)
            return new Result<>(Constants.CODE_200, "无数据", null);
        List<List<PublishImages>> publishImages = publishService.publishImages(publishList);
        return new Result<>(Constants.CODE_200, "成功", getPublishDTOs(publishList, publishImages));
    }

    @GetMapping("/publishHot")
    public Result<?> publishesAsHot(String page, String index) {
        List<Publish> publishList = publishService.publishAsHot(page, index);
        if (publishList.size() == 0)
            return new Result<>(Constants.CODE_200, "无数据", null);
        List<List<PublishImages>> publishImages = publishService.publishImages(publishList);
        return new Result<>(Constants.CODE_200, "请求成功", getPublishDTOs(publishList, publishImages));
    }

    private List<PublishDTO> getPublishDTOs(List<Publish> publishList, List<List<PublishImages>> publishImages) {
        List<PublishDTO> publishDTOS = new ArrayList<>();
        for (Publish publish : publishList) {
            int flag = 0;
            for (List<PublishImages> publishImage : publishImages) {
                if (publishImage.size() != 0 && publish.getId().equals(publishImage.get(0).getPublishId())) {
                    List<String> urls = publishImage.stream()
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
