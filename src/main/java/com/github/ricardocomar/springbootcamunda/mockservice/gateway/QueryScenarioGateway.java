package com.github.ricardocomar.springbootcamunda.mockservice.gateway;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.github.ricardocomar.springbootcamunda.mockservice.gateway.entity.ScenarioEntity;
import com.github.ricardocomar.springbootcamunda.mockservice.gateway.mapper.ScenarioEntityMapper;
import com.github.ricardocomar.springbootcamunda.mockservice.gateway.repository.ScenarioRepository;
import com.github.ricardocomar.springbootcamunda.mockservice.model.Scenario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QueryScenarioGateway {

    @Autowired
    private ScenarioRepository repository;

    @Autowired
    private ScenarioEntityMapper mapper;

    public Optional<Scenario> query(String topicName, String scenarioId) {

        Optional<ScenarioEntity> scenario = repository.findById(scenarioId);
        return scenario.map(e -> Optional.of(mapper.fromEntity(e)).orElse(null));
    }

    public List<Scenario> query(String topicName) {
        return repository.findByTopicName(topicName).stream().map(e -> mapper.fromEntity(e))
                .collect(Collectors.toList());
    }

    public Optional<Scenario> query(String topicName, Integer priority) {

        Optional<ScenarioEntity> scenario = repository.findByTopicNameAndPriority(topicName, priority);
        return scenario.map(e -> Optional.of(mapper.fromEntity(e)).orElse(null));
    }
}
