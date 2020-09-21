package com.github.ricardocomar.springbootcamunda.mockservice.entrypoint.mapper;

import com.github.ricardocomar.springbootcamunda.mockservice.entrypoint.model.ConditionRequest;
import com.github.ricardocomar.springbootcamunda.mockservice.model.Condition;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConditionRequestMapper {

    public Condition fromRequest(ConditionRequest request);

}
