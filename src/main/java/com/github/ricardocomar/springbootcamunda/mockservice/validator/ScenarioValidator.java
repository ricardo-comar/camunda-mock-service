package com.github.ricardocomar.springbootcamunda.mockservice.validator;

import static br.com.fluentvalidator.predicate.LogicalPredicate.not;
import static br.com.fluentvalidator.predicate.ObjectPredicate.nullValue;
import static br.com.fluentvalidator.predicate.ComparablePredicate.greaterThanOrEqual;
import static br.com.fluentvalidator.predicate.StringPredicate.stringEmptyOrNull;
import com.github.ricardocomar.springbootcamunda.mockservice.model.Scenario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import br.com.fluentvalidator.AbstractValidator;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Component
@AllArgsConstructor
@NoArgsConstructor
public class ScenarioValidator extends AbstractValidator<Scenario> {

    @Autowired
    private VariableValidator varValidator;

    @Autowired
    private ConditionValidator condValidator;

    @Override
    public void rules() {

        ruleFor("topicName", Scenario::getTopicName).must(not(stringEmptyOrNull()))
                .withMessage("Topic is mandatory").critical();

        ruleFor("scenarioId", Scenario::getScenarioId).must(not(nullValue()))
                .withMessage("Scenario is mandatory").critical();

        ruleFor("order", Scenario::getOrder).must(greaterThanOrEqual(1L))
                .withMessage("Order is mandatory").critical();

        ruleFor("condition", Scenario::getCondition).must(not(nullValue()))
                .withMessage("Condition is mandatory").whenever(not(nullValue()))
                .withValidator(condValidator).critical();

        ruleForEach("variables", Scenario::getVariables).whenever(not(nullValue()))
                .withValidator(varValidator).critical();

    }
}
