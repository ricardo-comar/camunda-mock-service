package com.github.ricardocomar.camunda.mockservice.gateway.mapper;

import com.github.ricardocomar.camunda.mockservice.gateway.entity.FailureEmbeddable;
import com.github.ricardocomar.camunda.mockservice.model.ScenarioFailure;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FailureEmbeddableMapper {

    public ScenarioFailure fromEntity(FailureEmbeddable entity);

    public FailureEmbeddable fromModel(ScenarioFailure model);

}
