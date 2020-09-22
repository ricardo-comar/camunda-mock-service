package com.github.ricardocomar.camunda.mockservice.usecase;

import com.github.ricardocomar.camunda.mockservice.gateway.SaveScenarioGateway;
import com.github.ricardocomar.camunda.mockservice.model.Scenario;
import com.github.ricardocomar.camunda.mockservice.validator.CreateScenarioValidationException;
import com.github.ricardocomar.camunda.mockservice.validator.ScenarioValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import br.com.fluentvalidator.context.ValidationResult;

@Component
public class SaveScenarioUseCase {

    @Autowired
    private SaveScenarioGateway saveGateway;

    @Autowired
    private ScenarioValidator validator;

    public Scenario save(Scenario scenario) {

        ValidationResult validation = validator.validate(scenario);
        validation.isInvalidThrow(CreateScenarioValidationException.class);

        return saveGateway.save(scenario);
    }

}
