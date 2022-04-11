package com.sq.SYTreeHole.service.Impl.EmotionalAnalysis;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sq.SYTreeHole.dao.EmotionalAnalysisDao.EmotionalAnalysisMapper;
import com.sq.SYTreeHole.entity.Publish;
import com.sq.SYTreeHole.service.emotionalAnalysisService.EmotionalAnalysisService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmotionalAnalysisServiceImpl extends ServiceImpl<EmotionalAnalysisMapper, Publish> implements EmotionalAnalysisService {


    @Override
    public Integer lastMark(String userId) {
        return getById(userId).getMark();
    }

    @Override
    public List<Publish> allMark(String userId) {
        return null;
    }

    //TODO 追加...
}
