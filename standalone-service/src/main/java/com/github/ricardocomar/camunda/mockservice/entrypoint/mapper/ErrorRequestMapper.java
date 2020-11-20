package com.github.ricardocomar.camunda.mockservice.entrypoint.mapper;

import com.github.ricardocomar.camunda.mockservice.entrypoint.model.ErrorRequest;
import com.github.ricardocomar.camunda.mockservice.model.ScenarioError;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ErrorRequestMapper {

    public ScenarioError fromRequest(ErrorRequest request);

}
