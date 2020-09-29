package com.github.ricardocomar.camunda.mockservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScenarioError {

    private String errorCode;
    
    private String errorMessage;
}
