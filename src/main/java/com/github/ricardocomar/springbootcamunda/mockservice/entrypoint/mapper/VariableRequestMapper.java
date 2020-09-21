package com.github.ricardocomar.springbootcamunda.mockservice.entrypoint.mapper;

import com.github.ricardocomar.springbootcamunda.mockservice.entrypoint.model.VariableRequest;
import com.github.ricardocomar.springbootcamunda.mockservice.model.Variable;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VariableRequestMapper {

    public Variable fromRequest(VariableRequest request);

}
