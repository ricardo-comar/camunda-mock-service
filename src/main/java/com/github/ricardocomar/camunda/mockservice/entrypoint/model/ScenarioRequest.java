package com.github.ricardocomar.camunda.mockservice.entrypoint.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
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

    private DelayRequest delay;

    private FailureRequest failure;

    private ErrorRequest error;
    
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<VariableRequest> variables;

}
