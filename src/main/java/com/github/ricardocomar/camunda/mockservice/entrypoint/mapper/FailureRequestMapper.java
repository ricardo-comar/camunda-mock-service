package com.github.ricardocomar.camunda.mockservice.entrypoint.mapper;

import com.github.ricardocomar.camunda.mockservice.entrypoint.model.FailureRequest;
import com.github.ricardocomar.camunda.mockservice.model.ScenarioFailure;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FailureRequestMapper {

    public ScenarioFailure fromRequest(FailureRequest request);

}
