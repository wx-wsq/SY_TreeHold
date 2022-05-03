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

    /**
     *
     * @param notice 通知实体
     * 需要的参数：
     *               userId:点赞/评论的用户的id
     *               publishId:当前动态的id
     *               publishUserId:当前动态所属用户的id
     *               index:0为点赞，1为评论
     * @return 结果
     */
    @PostMapping("setNotice")
    public Result<?> setNotice(Notice notice){
        noticeService.setMyNotice(notice);
        return new Result<>(Constants.CODE_200,"保存成功",null);
    }
}
