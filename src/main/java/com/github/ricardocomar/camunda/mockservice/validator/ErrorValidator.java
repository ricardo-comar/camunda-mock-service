package com.github.ricardocomar.camunda.mockservice.validator;

import static br.com.fluentvalidator.predicate.LogicalPredicate.not;
import static br.com.fluentvalidator.predicate.StringPredicate.stringEmptyOrNull;
import com.github.ricardocomar.camunda.mockservice.model.ScenarioError;
import org.springframework.stereotype.Component;
import br.com.fluentvalidator.AbstractValidator;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Component
public class ErrorValidator extends AbstractValidator<ScenarioError> {

    @Override
    public void rules() {

        ruleFor("details", ScenarioError::getErrorCode).must(not(stringEmptyOrNull()))
                .withMessage("Error Code is mandatory").critical();

        ruleFor("message", ScenarioError::getErrorMessage).must(not(stringEmptyOrNull()))
                .withMessage("Error Message is mandatory").critical();

    }

}
