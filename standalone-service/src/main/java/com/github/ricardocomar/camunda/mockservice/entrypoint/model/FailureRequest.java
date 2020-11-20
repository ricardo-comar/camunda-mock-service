package com.github.ricardocomar.camunda.mockservice.entrypoint.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FailureRequest {

    private String message;

    private String details;

    private Integer retryTimes;

    private Long retryTimeout;
}
