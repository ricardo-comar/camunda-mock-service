package com.github.ricardocomar.camunda.mockservice.gateway.entity;

import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class DelayEmbeddable {

    private Integer fixedMs;

    private Integer minMs;

    private Integer maxMs;

}
