package com.github.ricardocomar.springbootcamunda.mockservice.gateway.mapper;

import com.github.ricardocomar.springbootcamunda.mockservice.gateway.entity.ScenarioEntity;
import com.github.ricardocomar.springbootcamunda.mockservice.model.Scenario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {VariableEntityMapper.class, ConditionEntityMapper.class})
public interface ScenarioEntityMapper {

    public Scenario fromEntity(ScenarioEntity entity);

    public ScenarioEntity fromModel(Scenario model);

}
