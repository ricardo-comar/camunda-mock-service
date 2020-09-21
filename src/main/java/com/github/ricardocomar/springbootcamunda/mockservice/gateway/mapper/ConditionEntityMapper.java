package com.github.ricardocomar.springbootcamunda.mockservice.gateway.mapper;

import com.github.ricardocomar.springbootcamunda.mockservice.gateway.entity.ConditionEntity;
import com.github.ricardocomar.springbootcamunda.mockservice.model.Condition;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConditionEntityMapper {

    public Condition fromEntity(ConditionEntity entity);

}
