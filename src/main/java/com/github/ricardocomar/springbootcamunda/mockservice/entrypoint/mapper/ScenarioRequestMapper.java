package com.github.ricardocomar.springbootcamunda.mockservice.entrypoint.mapper;

import com.github.ricardocomar.springbootcamunda.mockservice.entrypoint.model.ScenarioRequest;
import com.github.ricardocomar.springbootcamunda.mockservice.model.Scenario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {VariableRequestMapper.class, ConditionRequestMapper.class})
public interface ScenarioRequestMapper {

    public Scenario fromRequest(ScenarioRequest request);

}
