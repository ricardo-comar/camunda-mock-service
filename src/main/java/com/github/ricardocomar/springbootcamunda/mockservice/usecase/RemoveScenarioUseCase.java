package com.github.ricardocomar.springbootcamunda.mockservice.usecase;

import com.github.ricardocomar.springbootcamunda.mockservice.gateway.RemoveScenarioGateway;
import com.github.ricardocomar.springbootcamunda.mockservice.model.Scenario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RemoveScenarioUseCase {

    @Autowired
    private RemoveScenarioGateway removeGateway;

    public void remove(Scenario scenario) {
        removeGateway.remove(scenario);
    }

}
