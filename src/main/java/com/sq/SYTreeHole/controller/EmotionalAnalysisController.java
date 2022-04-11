package com.sq.SYTreeHole.controller;

import com.sq.SYTreeHole.common.Result;
import com.sq.SYTreeHole.service.emotionalAnalysisService.EmotionalAnalysisService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class EmotionalAnalysisController {

    @Resource
    private EmotionalAnalysisService ea;


    @GetMapping("/EA/{userId}")
    public Result<?> emotionalAnalysisAll(@PathVariable String userId){
        return new Result<>();
    }
}
