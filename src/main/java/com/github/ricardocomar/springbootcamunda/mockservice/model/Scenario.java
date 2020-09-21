package com.github.ricardocomar.springbootcamunda.mockservice.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Scenario {

    private String scenarioId;

    private String topicName;

    private Condition condition;    

    private Long priority;

    private List<Variable> variables;
}
