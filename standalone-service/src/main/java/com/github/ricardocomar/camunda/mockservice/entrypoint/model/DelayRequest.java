package com.github.ricardocomar.camunda.mockservice.entrypoint.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DelayRequest {

    private Integer fixedMs;

    private Integer minMs;

    private Integer maxMs;

}
