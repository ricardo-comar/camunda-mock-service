package com.github.ricardocomar.springbootcamunda.mockservice.validator;

import static br.com.fluentvalidator.predicate.LogicalPredicate.not;
import static br.com.fluentvalidator.predicate.StringPredicate.stringEmptyOrNull;
import com.github.ricardocomar.springbootcamunda.mockservice.model.Condition;
import org.springframework.stereotype.Component;
import br.com.fluentvalidator.AbstractValidator;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Component
public class ConditionValidator extends AbstractValidator<Condition> {

    @Override
    public void rules() {

        ruleFor("conditionScript", Condition::getConditionScript).must(not(stringEmptyOrNull()))
                .withMessage("conditionScript is mandatory").critical();

    }

}
