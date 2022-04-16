package com.sq.SYTreeHole.service.emotionalAnalysisService;

import com.sq.SYTreeHole.entity.Publish;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface EmotionalAnalysisService {

    Double lastMark(String userId);

    Map<? extends Serializable, ? extends Serializable> allMarkForLineChart(String userId);

    Map<? extends Serializable, ? extends Serializable>allMarkForPieChart(String userId);

}
