package com.github.ricardocomar.springbootcamunda.mockservice.entrypoint.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VariableRequest {
    
    private String name;

    private String value;

    private String className;

    private String groovyScript;

}
