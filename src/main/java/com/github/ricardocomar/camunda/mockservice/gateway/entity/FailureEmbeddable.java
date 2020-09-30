package com.github.ricardocomar.camunda.mockservice.gateway.entity;

import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class FailureEmbeddable {

    private String message;

    private String details;

    private Integer retryTimes;

    private Long retryTimeout;
}
