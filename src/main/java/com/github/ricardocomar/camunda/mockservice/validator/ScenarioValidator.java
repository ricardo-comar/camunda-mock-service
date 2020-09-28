package com.github.ricardocomar.camunda.mockservice.validator;

import static br.com.fluentvalidator.predicate.CollectionPredicate.empty;
import static br.com.fluentvalidator.predicate.ComparablePredicate.greaterThanOrEqual;
import static br.com.fluentvalidator.predicate.LogicalPredicate.not;
import static br.com.fluentvalidator.predicate.ObjectPredicate.nullValue;
import static br.com.fluentvalidator.predicate.StringPredicate.stringEmptyOrNull;
import com.github.ricardocomar.camunda.mockservice.model.Scenario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import br.com.fluentvalidator.AbstractValidator;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public class ScenarioValidator extends AbstractValidator<Scenario> {

    @Autowired
    private VariableValidator varValidator;

    @Autowired
    private ConditionValidator condValidator;

    @Autowired
    private DelayValidator delayValidator;

    @Override
    public void rules() {

        ruleFor("topicName", Scenario::getTopicName).must(not(stringEmptyOrNull()))
                .withMessage("Topic is mandatory").critical();

        ruleFor("priority", Scenario::getPriority).must(greaterThanOrEqual(1))
                .withMessage("Priority is mandatory").critical();

        ruleFor("condition", Scenario::getCondition).must(not(nullValue()))
                .withMessage("Condition is mandatory").whenever(not(nullValue()))
                .withValidator(condValidator).critical();

        ruleFor("delay", Scenario::getDelay).whenever(not(nullValue()))
                .withValidator(delayValidator).critical();

        ruleForEach("variables", Scenario::getVariables).must(not(empty()))
                .withMessage("Variables is mandatory").whenever(not(nullValue()))
                .withValidator(varValidator).critical();

    }
}
