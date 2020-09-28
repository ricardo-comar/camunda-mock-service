package com.github.ricardocomar.camunda.mockservice.gateway.mapper;

import com.github.ricardocomar.camunda.mockservice.gateway.entity.ScenarioEntity;
import com.github.ricardocomar.camunda.mockservice.model.Scenario;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {VariableEntityMapper.class, ConditionEntityMapper.class,
        DelayEmbeddableMapper.class, FailureEmbeddableMapper.class})
public abstract class ScenarioEntityMapper {

    public abstract Scenario fromEntity(ScenarioEntity entity);

    public abstract ScenarioEntity fromModel(Scenario model);

    @AfterMapping
    protected void enrichCycleReference(@MappingTarget ScenarioEntity entity) {
        entity.getVariables().stream().forEach(v -> v.setScenario(entity));
    }
}
