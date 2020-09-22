package com.github.ricardocomar.camunda.mockservice.gateway.repository;

import com.github.ricardocomar.camunda.mockservice.gateway.entity.VariableEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * OrderRepository
 */
public interface VariableRepository extends JpaRepository<VariableEntity, Long>{
}