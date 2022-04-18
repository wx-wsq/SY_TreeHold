package com.sq.SYTreeHole.controller;

import com.sq.SYTreeHole.DTO.EmotionalAnalysisDTO;
import com.sq.SYTreeHole.common.Constants;
import com.sq.SYTreeHole.common.Result;
import com.sq.SYTreeHole.service.emotionalAnalysisService.EmotionalAnalysisService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Map;

@RestController
public class EmotionalAnalysisController {

    @Resource
    private EmotionalAnalysisService ea;


    @GetMapping("/EA/{userId}")
    public Result<?> emotionalAnalysisAll(@PathVariable String userId){
        Double mark = ea.lastMark(userId);
        Map<? extends Serializable, ? extends Serializable> allMarkForLineChart = ea.allMarkForLineChart(userId);
        Map<? extends Serializable, ? extends Serializable> allMarkForPieChart = ea.allMarkForPieChart(userId);
        EmotionalAnalysisDTO emotionalAnalysisDTO = new EmotionalAnalysisDTO();
        emotionalAnalysisDTO
                .setMark(mark)
                .setLineChartData(allMarkForLineChart)
                .setPieChartData(allMarkForPieChart);
        return new Result<>(Constants.CODE_200,"请求成功",emotionalAnalysisDTO);
    }

}
