package com.github.ricardocomar.camunda.mockservice.validator;

import static br.com.fluentvalidator.predicate.LogicalPredicate.not;
import static br.com.fluentvalidator.predicate.StringPredicate.stringEmptyOrNull;
import static br.com.fluentvalidator.predicate.ComparablePredicate.greaterThan;
import static br.com.fluentvalidator.predicate.ObjectPredicate.nullValue;
import com.github.ricardocomar.camunda.mockservice.model.ScenarioFailure;
import org.springframework.stereotype.Component;
import br.com.fluentvalidator.AbstractValidator;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Component
public class FailureValidator extends AbstractValidator<ScenarioFailure> {

    @Override
    public void rules() {

        ruleFor("details", ScenarioFailure::getDetails).must(not(stringEmptyOrNull()))
                .withMessage("Details is mandatory").critical();

        ruleFor("message", ScenarioFailure::getMessage).must(not(stringEmptyOrNull()))
                .withMessage("Message is mandatory").critical();

        ruleFor("retryTimes", ScenarioFailure::getRetryTimes).must(greaterThan(0)).when(not(nullValue()))
                .withMessage("Message is mandatory").critical();

        ruleFor("retryTimeout", ScenarioFailure::getRetryTimeout).must(greaterThan(0L)).when(not(nullValue()))
                .withMessage("Message is mandatory").critical();

    }

}
