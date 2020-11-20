package com.github.ricardocomar.camunda.mockservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Variable {

    private String name;

    private String value;

    private String className;

    private String groovyScript;

}
