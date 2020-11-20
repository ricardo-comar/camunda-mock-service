package com.github.ricardocomar.camunda.mockservice.entrypoint.mapper;

import com.github.ricardocomar.camunda.mockservice.entrypoint.model.ConditionRequest;
import com.github.ricardocomar.camunda.mockservice.model.Condition;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConditionRequestMapper {

    public Condition fromRequest(ConditionRequest request);

}
