package com.github.ricardocomar.camunda.mockservice.entrypoint.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScenarioRequest {

    private String topicName;

    private ConditionRequest condition;    

    private Integer priority;
    
    private List<VariableRequest> variables;

}
