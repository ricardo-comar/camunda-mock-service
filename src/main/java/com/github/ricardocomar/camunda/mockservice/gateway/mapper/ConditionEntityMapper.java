package com.github.ricardocomar.camunda.mockservice.gateway.mapper;

import com.github.ricardocomar.camunda.mockservice.gateway.entity.ConditionEntity;
import com.github.ricardocomar.camunda.mockservice.model.Condition;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConditionEntityMapper {

    public Condition fromEntity(ConditionEntity entity);

}
