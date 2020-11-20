package com.github.ricardocomar.camunda.mockservice.gateway.mapper;

import com.github.ricardocomar.camunda.mockservice.gateway.entity.DelayEmbeddable;
import com.github.ricardocomar.camunda.mockservice.model.Delay;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DelayEmbeddableMapper {

    public Delay fromEmbeddable(DelayEmbeddable embeddable);

    public DelayEmbeddable fromModel(Delay model);

}
