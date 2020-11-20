package com.github.ricardocomar.camunda.mockservice.entrypoint.mapper;

import com.github.ricardocomar.camunda.mockservice.entrypoint.model.ScenarioRequest;
import com.github.ricardocomar.camunda.mockservice.model.Scenario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {VariableRequestMapper.class, ConditionRequestMapper.class, DelayRequestMapper.class
, FailureRequestMapper.class, ErrorRequestMapper.class})
public interface ScenarioRequestMapper {

    public Scenario fromRequest(ScenarioRequest request);

}
