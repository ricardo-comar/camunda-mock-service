package com.github.ricardocomar.camunda.mockservice.gateway.mapper;

import com.github.ricardocomar.camunda.mockservice.gateway.entity.ErrorEmbeddable;
import com.github.ricardocomar.camunda.mockservice.model.ScenarioError;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ErrorEmbeddableMapper {

    public ScenarioError fromEntity(ErrorEmbeddable entity);

    public ErrorEmbeddable fromModel(ScenarioError model);

}
