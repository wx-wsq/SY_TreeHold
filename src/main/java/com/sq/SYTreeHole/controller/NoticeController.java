package com.sq.SYTreeHole.controller;

import com.sq.SYTreeHole.common.Constants;
import com.sq.SYTreeHole.common.Result;
import com.sq.SYTreeHole.entity.Notice;
import com.sq.SYTreeHole.service.publishService.NoticeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.List;

@RestController
public class NoticeController {

    @Resource
    private NoticeService noticeService;

    @GetMapping("notices")
    public Result<?> findMyNotice(String userId){
        List<Notice> myNotices = noticeService.findMyNotices(userId);
        return new Result<>(Constants.CODE_200,"请求成功",myNotices);
    }

    @PostMapping("setNotice")
    public Result<?> setNotice(Notice notice){
        noticeService.setMyNotice(notice);
        return new Result<>(Constants.CODE_200,"保存成功",null);
    }
}
