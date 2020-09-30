package com.github.ricardocomar.camunda.mockservice.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
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

    private Integer priority;

    private Delay delay;

    private ScenarioFailure failure;

    private ScenarioError error;

    @JsonInclude(Include.NON_EMPTY)
    private List<Variable> variables;
}
