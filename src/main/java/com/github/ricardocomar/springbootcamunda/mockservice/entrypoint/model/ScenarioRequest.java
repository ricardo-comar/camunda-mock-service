package com.github.ricardocomar.springbootcamunda.mockservice.entrypoint.model;

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

    private Long priority;
    
    private List<VariableRequest> variables;

}
