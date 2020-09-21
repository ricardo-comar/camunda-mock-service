package com.github.ricardocomar.springbootcamunda.mockservice.gateway.mapper;

import com.github.ricardocomar.springbootcamunda.mockservice.gateway.entity.VariableEntity;
import com.github.ricardocomar.springbootcamunda.mockservice.model.Variable;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VariableEntityMapper {

    public Variable fromEntity(VariableEntity entity);

}
