package com.sq.SYTreeHole.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class EmotionalAnalysis {

    private Integer mark;
    private Map<String, String> lineChartData;
    private Map<String, String> pieChartData;
}
