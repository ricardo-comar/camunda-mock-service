package com.github.ricardocomar.camunda.mockservice.validator;

import static br.com.fluentvalidator.predicate.ComparablePredicate.greaterThan;
import static br.com.fluentvalidator.predicate.LogicalPredicate.not;
import static br.com.fluentvalidator.predicate.ObjectPredicate.nullValue;
import com.github.ricardocomar.camunda.mockservice.model.Delay;
import org.springframework.stereotype.Component;
import br.com.fluentvalidator.AbstractValidator;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Component
public class DelayValidator extends AbstractValidator<Delay> {

    @Override
    public void rules() {

        ruleFor("fixedMs", Delay::getFixedMs).must(greaterThan(0))
                .when(not(nullValue()))
                .withMessage("fixedMs must be positive").critical();
        ruleFor("minMs", Delay::getMinMs).must(greaterThan(0))
                .when(not(nullValue()))
                .withMessage("minMs must be positive").critical();
        ruleFor("maxMs", Delay::getMaxMs).must(greaterThan(0))
                .when(not(nullValue()))
                .withMessage("maxMs must be positive").critical();
        ruleFor(root -> root).must( d -> d.getMaxMs().compareTo(d.getMinMs()) > 0)
                .when(not(nullValue(Delay::getMinMs)).and(not(nullValue(Delay::getMaxMs))))
                .withMessage("minMs must be positive").critical();

        ruleFor(root -> root)
                // XOR = ((A && notB) || (notA && B))
                .must(not(nullValue(Delay::getMinMs))
                        .and(not(nullValue(Delay::getMaxMs)))
                        .and(not(not(nullValue(Delay::getFixedMs))))
                        .or(not(not(nullValue(Delay::getMinMs))
                                .and(not(nullValue(Delay::getMaxMs))))
                                        .and(not(nullValue(Delay::getFixedMs)))))
                .withMessage("fixedMs or minMs + maxMs are mandatory")
                .critical();

    }

}
