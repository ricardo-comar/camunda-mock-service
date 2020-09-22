package com.github.ricardocomar.springbootcamunda.mockservice.usecase;

import com.github.ricardocomar.springbootcamunda.mockservice.gateway.RemoveScenarioGateway;
import com.github.ricardocomar.springbootcamunda.mockservice.gateway.SaveScenarioGateway;
import com.github.ricardocomar.springbootcamunda.mockservice.model.Scenario;
import com.github.ricardocomar.springbootcamunda.mockservice.validator.CreateScenarioValidationException;
import com.github.ricardocomar.springbootcamunda.mockservice.validator.ScenarioValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import br.com.fluentvalidator.context.ValidationResult;

@Component
public class SaveScenarioUseCase {

    @Autowired
    private RemoveScenarioGateway removeGateway;

    @Autowired
    private SaveScenarioGateway saveGateway;

    @Autowired
    private ScenarioValidator validator;

    public Scenario save(Scenario scenario) {

        ValidationResult validation = validator.validate(scenario);
        validation.isInvalidThrow(CreateScenarioValidationException.class);

        if (StringUtils.isNotEmpty(scenario.getScenarioId())) {
            removeGateway.remove(scenario);
        }

        return saveGateway.save(scenario);
    }

}
