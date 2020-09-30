package com.github.ricardocomar.camunda.mockservice.validator;

import br.com.fluentvalidator.context.ValidationResult;
import br.com.fluentvalidator.exception.ValidationException;

public class DelayValidationException extends ValidationException {

    private static final long serialVersionUID = -671261466512160049L;

    public DelayValidationException(final ValidationResult validationResult) {
        super(validationResult);
    }
}
