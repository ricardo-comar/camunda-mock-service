package com.github.ricardocomar.camunda.mockservice.entrypoint.mapper;

import com.github.ricardocomar.camunda.mockservice.entrypoint.model.FailureRequest;
import com.github.ricardocomar.camunda.mockservice.model.Failure;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FailureRequestMapper {

    public Failure fromRequest(FailureRequest request);

}
