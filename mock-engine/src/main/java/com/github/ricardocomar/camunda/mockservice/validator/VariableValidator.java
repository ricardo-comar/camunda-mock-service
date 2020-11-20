package com.github.ricardocomar.camunda.mockservice.validator;

import static br.com.fluentvalidator.predicate.LogicalPredicate.not;
import static br.com.fluentvalidator.predicate.StringPredicate.stringEmptyOrNull;
import com.github.ricardocomar.camunda.mockservice.model.Variable;
import org.springframework.stereotype.Component;
import br.com.fluentvalidator.AbstractValidator;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Component
public class VariableValidator extends AbstractValidator<Variable> {

    @Override
    public void rules() {

        ruleFor(root -> root).must(not(stringEmptyOrNull(Variable::getName)))
                .withMessage("Variable Name is mandatory").critical();

        ruleFor(root -> root) //TODO: Eval to fluent validator XOR
                // XOR = ((A && notB) || (notA && B))
                .must(stringEmptyOrNull(Variable::getClassName)
                        .and(stringEmptyOrNull(Variable::getValue))
                        .and(not(stringEmptyOrNull(Variable::getGroovyScript)))
                        .or(not(stringEmptyOrNull(Variable::getClassName)
                                .and(stringEmptyOrNull(Variable::getValue)))
                                        .and(stringEmptyOrNull(Variable::getGroovyScript))))
                .withMessage("groovyScript or Class Name + value are mandatory")
                .critical();

    }

}
