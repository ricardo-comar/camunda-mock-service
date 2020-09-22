package com.github.ricardocomar.camunda.mockservice.gateway.repository;

import java.util.List;
import java.util.Optional;
import com.github.ricardocomar.camunda.mockservice.gateway.entity.ScenarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * OrderRepository
 */
public interface ScenarioRepository extends JpaRepository<ScenarioEntity, String>{

    public List<ScenarioEntity> findByTopicName(String topicName);

    public Optional<ScenarioEntity> findByTopicNameAndPriority(String topicName, Integer priority);

}