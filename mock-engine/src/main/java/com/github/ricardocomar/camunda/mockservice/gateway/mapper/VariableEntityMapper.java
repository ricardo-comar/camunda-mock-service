package com.github.ricardocomar.camunda.mockservice.gateway.mapper;

import com.github.ricardocomar.camunda.mockservice.gateway.entity.VariableEntity;
import com.github.ricardocomar.camunda.mockservice.model.Variable;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VariableEntityMapper {

    public Variable fromEntity(VariableEntity entity);

    public VariableEntity fromModel(Variable model);

}
