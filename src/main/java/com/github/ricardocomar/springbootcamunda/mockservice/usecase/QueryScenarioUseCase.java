package com.github.ricardocomar.springbootcamunda.mockservice.usecase;

import java.util.List;
import java.util.Optional;
import com.github.ricardocomar.springbootcamunda.mockservice.gateway.QueryScenarioGateway;
import com.github.ricardocomar.springbootcamunda.mockservice.model.Scenario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QueryScenarioUseCase {

    @Autowired
    private QueryScenarioGateway gateway;

    public Optional<Scenario> queryScenario(String topicName, String scenarioId) {
        return gateway.query(topicName, scenarioId);
    }
    
    public List<Scenario> queryScenarios(String topicName) {
        return gateway.query(topicName);
    }
    
}
