package com.github.ricardocomar.camunda.mockservice.gateway.mapper;

import com.github.ricardocomar.camunda.mockservice.gateway.entity.ScenarioEntity;
import com.github.ricardocomar.camunda.mockservice.model.Scenario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {VariableEntityMapper.class, ConditionEntityMapper.class})
public interface ScenarioEntityMapper {

    public Scenario fromEntity(ScenarioEntity entity);

    public ScenarioEntity fromModel(Scenario model);

}
