package com.github.ricardocomar.springbootcamunda.mockservice.gateway.repository;

import java.util.List;
import com.github.ricardocomar.springbootcamunda.mockservice.gateway.entity.ScenarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * OrderRepository
 */
public interface ScenarioRepository extends JpaRepository<ScenarioEntity, String>{

    public List<ScenarioEntity> findByTopicName(String topicName);

}