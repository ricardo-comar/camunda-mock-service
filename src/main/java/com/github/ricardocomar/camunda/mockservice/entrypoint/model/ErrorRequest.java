package com.github.ricardocomar.camunda.mockservice.entrypoint.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorRequest {

    private String errorCode;
    
    private String errorMessage;

}
