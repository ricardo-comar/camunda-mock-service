package com.github.ricardocomar.camunda.mockservice.entrypoint.mapper;

import com.github.ricardocomar.camunda.mockservice.entrypoint.model.DelayRequest;
import com.github.ricardocomar.camunda.mockservice.model.Delay;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DelayRequestMapper {

    public Delay fromRequest(DelayRequest request);

}
