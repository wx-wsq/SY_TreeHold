package com.sq.SYTreeHole.service.Impl.EmotionalAnalysis;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sq.SYTreeHole.dao.EmotionalAnalysisDao.EmotionalAnalysisMapper;
import com.sq.SYTreeHole.entity.Publish;
import com.sq.SYTreeHole.exception.EmotionalAnalysisException;
import com.sq.SYTreeHole.service.emotionalAnalysisService.EmotionalAnalysisService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmotionalAnalysisServiceImpl extends ServiceImpl<EmotionalAnalysisMapper, Publish> implements EmotionalAnalysisService {
    @Override
    public Double lastMark(String userId) {
        if (Strings.isBlank(userId))
            throw new EmotionalAnalysisException("空参异常");
        return getBaseMapper().lastMark(userId) *100;
    }

    @Override
    public Map<? extends Serializable, ? extends Serializable> allMarkForLineChart(String userId) {
        if (Strings.isBlank(userId))
            throw new EmotionalAnalysisException("空参异常");
        List<Publish> publishes = getBaseMapper()
                .allMarkForLineChart(userId)
                .stream()
                .map(v ->
                        v.setId(String.valueOf((System.currentTimeMillis() - v.getModifyTime().getTime()) / (24 * 60 * 60 * 1000))))
                .collect(Collectors.toList());
        for (int i = 0; i < publishes.size(); i++)
            for (int j = i + 1; j < publishes.size(); j++)
                if (publishes.get(i).getId().equals(publishes.get(j).getId())) {
                    Publish publish = publishes
                            .get(i)
                            .setMark((publishes.get(i).getMark() + publishes.get(j).getMark()) / 2);
                    publishes.remove(i--);
                    publishes.remove(--j);
                    publishes.add(publish);
                    if(i == -1) i=0;
                }
        return publishes.stream().collect(Collectors.toMap(Publish::getId, Publish::getMark));
    }

    @Override
    public Map<? extends Serializable, ? extends Serializable> allMarkForPieChart(String userId) {
        if (Strings.isBlank(userId))
            throw new EmotionalAnalysisException("空参异常");
        List<Publish> publishes = getBaseMapper().allMarkForPieChart(userId);
        HashMap<String, Double> result = new HashMap<>();
        for (Publish publish : publishes) {
            if (result.containsKey(publish.getId()))
                result.put(publish.getId(), result.get(publish.getId()) + 1);
            else
                result.put(publish.getId(), 1.0);
        }
        result.replaceAll((s, v) -> v / publishes.size());
        return result;
    }
}
