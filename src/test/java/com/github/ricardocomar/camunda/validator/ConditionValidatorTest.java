package com.github.ricardocomar.camunda.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import com.github.ricardocomar.camunda.mockservice.model.Condition;
import com.github.ricardocomar.camunda.mockservice.validator.ConditionValidator;
import org.junit.Ignore;
import org.junit.Test;

public class ConditionValidatorTest {

    private ConditionValidator validator = new ConditionValidator();

    @Test
    public void testValid() {

        assertThat(validator.validate(new Condition("return true")).isValid(), is(true));
    }

    @Test
    public void testEmpty() {

        assertThat(validator.validate(new Condition(null)).isValid(), is(false));
    }

    @Test @Ignore("validate expression")
    public void testInvalidExpression() {

        assertThat(validator.validate(new Condition("asecas das asd")).isValid(), is(true));
    }

}
