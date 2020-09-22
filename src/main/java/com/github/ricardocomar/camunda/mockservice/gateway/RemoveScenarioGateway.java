package com.github.ricardocomar.camunda.mockservice.gateway;

import java.util.Optional;
import com.github.ricardocomar.camunda.mockservice.gateway.entity.ScenarioEntity;
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

        Optional<ScenarioEntity> scenarioOpt = scenarioRepo
                .findById(scenario.getScenarioId());

        if (scenarioOpt.isPresent()) {
            varRepo.deleteAll(scenarioOpt.get().getVariables());
            scenarioRepo.delete(scenarioOpt.get());
        }

    }
}
