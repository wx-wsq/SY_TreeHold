package com.sq.SYTreeHole.controller;

import com.sq.SYTreeHole.DTO.PublishDTO;
import com.sq.SYTreeHole.common.Constants;
import com.sq.SYTreeHole.common.Result;
import com.sq.SYTreeHole.entity.Publish;
import com.sq.SYTreeHole.entity.PublishImages;
import com.sq.SYTreeHole.service.publishService.PublishDetailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PublishDetailController {

    private final PublishDetailService publishDetailService;

    public PublishDetailController(PublishDetailService publishDetailService) {
        this.publishDetailService = publishDetailService;
    }

    @GetMapping("/publishDetail/{id}")
    public Result<PublishDTO> detail(@PathVariable String id) {
        Publish detail = publishDetailService.detail(id);
        List<PublishImages> imagesList = publishDetailService.publishImages(detail.getId());
        return new Result<>(Constants.CODE_200, "请求成功", getPublishDTO(detail, imagesList));
    }

    @GetMapping("/publishDetailIncrStar")
    public void IncrStar(String id) {
        publishDetailService.star(id, 1);
    }

    @GetMapping("/publishDetailDecrStar")
    public void DecrStar(String id) {
        publishDetailService.star(id, -1);
    }

    public PublishDTO getPublishDTO(Publish publish, List<PublishImages> publishImages) {
        if (publishImages == null)
            return new PublishDTO(publish,null);
        else
            return new PublishDTO(
                    publish,
                    publishImages
                            .stream()
                            .map(PublishImages::getUrl)
                            .collect(Collectors.toList()));
    }

}
