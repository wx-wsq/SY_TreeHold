package com.sq.SYTreeHole.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class EmotionalAnalysisDTO {

    private Double mark;
    private Map<? extends Serializable, ? extends Serializable> lineChartData;
    private Map<? extends Serializable, ? extends Serializable> pieChartData;
}
