package com.github.ricardocomar.camunda.mockservice.validator;

import br.com.fluentvalidator.context.ValidationResult;
import br.com.fluentvalidator.exception.ValidationException;

public class CreateScenarioValidationException extends ValidationException {

    private static final long serialVersionUID = -368100656086320660L;

    public CreateScenarioValidationException(final ValidationResult validationResult) {
        super(validationResult);
    }
}
