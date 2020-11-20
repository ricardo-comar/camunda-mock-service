package com.github.ricardocomar.camunda.mockservice.gateway;

import java.util.Optional;
import java.util.UUID;
import com.github.ricardocomar.camunda.mockservice.gateway.entity.ScenarioEntity;
import com.github.ricardocomar.camunda.mockservice.gateway.mapper.ScenarioEntityMapper;
import com.github.ricardocomar.camunda.mockservice.gateway.repository.ScenarioRepository;
import com.github.ricardocomar.camunda.mockservice.model.Scenario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SaveScenarioGateway {

    @Autowired
    private ScenarioRepository repository;

    @Autowired
    private ScenarioEntityMapper mapper;

    public Scenario save(Scenario model) {

        ScenarioEntity entity = mapper.fromModel(model);
        entity.setScenarioId(UUID.randomUUID().toString());

        Optional.ofNullable(entity.getVariables())
                .ifPresent(l -> l.forEach(v -> v.setScenario(entity)));

        return mapper.fromEntity(repository.saveAndFlush(entity));
    }
}
