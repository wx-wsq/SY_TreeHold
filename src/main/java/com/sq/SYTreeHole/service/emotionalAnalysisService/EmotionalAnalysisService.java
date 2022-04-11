package com.sq.SYTreeHole.service.emotionalAnalysisService;

import com.sq.SYTreeHole.entity.Publish;

import java.util.List;

public interface EmotionalAnalysisService {

    Integer lastMark(String userId);

    List<Publish> allMark(String userId);

}
