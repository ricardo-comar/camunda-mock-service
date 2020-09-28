package com.github.ricardocomar.camunda.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import com.github.ricardocomar.camunda.mockservice.model.Delay;
import com.github.ricardocomar.camunda.mockservice.validator.DelayValidationException;
import com.github.ricardocomar.camunda.mockservice.validator.DelayValidator;
import org.junit.Test;
import br.com.fluentvalidator.exception.ValidationException;

public class DelayValidatorTest {

    private DelayValidator validator = new DelayValidator();

    @Test
    public void testValidMinMax() {

        validator.validate(new Delay(null, 100, 300))
                .isInvalidThrow(DelayValidationException.class);
    }

    @Test
    public void testValidFixed() {

        validator.validate(new Delay(100, null, null))
                .isInvalidThrow(DelayValidationException.class);
    }

    @Test(expected = ValidationException.class)
    public void testInvalidAllValues() {

        validator.validate(new Delay(200, 100, 300)).isInvalidThrow(DelayValidationException.class);
    }

    @Test(expected = ValidationException.class)
    public void testInvalidNone() {

        validator.validate(new Delay(null, null, null))
                .isInvalidThrow(DelayValidationException.class);
    }

    @Test(expected = ValidationException.class)
    public void testInvalidRange() {

        validator.validate(new Delay(null, 300, 100))
                .isInvalidThrow(DelayValidationException.class);
    }

    @Test
    public void testInvalidNegative() {

        assertThat(validator.validate(new Delay(null, -100, 100)).isValid(), is(false));
        assertThat(validator.validate(new Delay(null, -100, -50)).isValid(), is(false));
        assertThat(validator.validate(new Delay(-100, null, null)).isValid(), is(false));
    }
}
