package com.github.ricardocomar.camunda.mockservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Failure {

    private String message;

    private String details;

    private Integer retryTimes;

    private Integer retryTimeout;

}
