package com.github.ricardocomar.camunda.mockservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Delay {

    private Integer fixedMs;

    private Integer minMs;

    private Integer maxMs;

}
