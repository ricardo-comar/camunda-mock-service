package com.github.ricardocomar.camunda.mockservice.entrypoint.mapper;

import com.github.ricardocomar.camunda.mockservice.entrypoint.model.VariableRequest;
import com.github.ricardocomar.camunda.mockservice.model.Variable;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VariableRequestMapper {

    public Variable fromRequest(VariableRequest request);

}
