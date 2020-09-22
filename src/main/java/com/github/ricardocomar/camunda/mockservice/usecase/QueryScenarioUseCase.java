package com.github.ricardocomar.camunda.mockservice.usecase;

import java.util.List;
import java.util.Optional;
import com.github.ricardocomar.camunda.mockservice.gateway.QueryScenarioGateway;
import com.github.ricardocomar.camunda.mockservice.model.Scenario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QueryScenarioUseCase {

    @Autowired
    private QueryScenarioGateway gateway;

    public Optional<Scenario> queryDuplicated(String topicName, Integer priority) {
        return gateway.query(topicName, priority);
    }

    public Optional<Scenario> queryScenario(String topicName, String scenarioId) {
        return gateway.query(topicName, scenarioId);
    }
    
    public List<Scenario> queryScenarios(String topicName) {
        return gateway.query(topicName);
    }
    
}
