package com.github.ricardocomar.camunda.mockservice.gateway;

import com.github.ricardocomar.camunda.mockservice.gateway.repository.ScenarioRepository;
import com.github.ricardocomar.camunda.mockservice.gateway.repository.VariableRepository;
import com.github.ricardocomar.camunda.mockservice.model.Scenario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RemoveScenarioGateway {

    @Autowired
    private ScenarioRepository scenarioRepo;

    @Autowired
    private VariableRepository varRepo;

    public void remove(Scenario scenario) {

        scenarioRepo.findById(scenario.getScenarioId()).ifPresent(s -> {
            varRepo.deleteAll(s.getVariables());
            scenarioRepo.delete(s);
        });

    }
}
