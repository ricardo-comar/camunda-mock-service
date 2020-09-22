package com.github.ricardocomar.camunda.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import com.github.ricardocomar.camunda.mockservice.model.Variable;
import com.github.ricardocomar.camunda.mockservice.validator.VariableValidator;
import static org.hamcrest.Matchers.is;
import org.junit.Test;
import br.com.fluentvalidator.context.ValidationResult;

public class VariableValidatorTest {

    private VariableValidator validator = new VariableValidator();

    @Test
    public void testValidValueClassName() {

        Variable variable = new Variable("name", "value", "className", null);
        ValidationResult result = validator.validate(variable);
        assertThat(result, notNullValue());
        assertThat(result.getErrors(), hasSize(0));
    }

    @Test
    public void testValidGroovyScript() {

        Variable variable = new Variable("name", null, null, "script");
        ValidationResult result = validator.validate(variable);
        assertThat(result, notNullValue());
        assertThat(result.getErrors(), hasSize(0));
    }

    @Test
    public void testInvalidAllTypes() {

        Variable variable = new Variable("name", "name", "className", "script");
        ValidationResult result = validator.validate(variable);
        assertThat(result, notNullValue());
        assertThat(result.isValid(), is(false));
    }

    @Test
    public void testInvalidNoType() {

        Variable variable = new Variable("name", null, null, null);
        ValidationResult result = validator.validate(variable);
        assertThat(result, notNullValue());
        assertThat(result.isValid(), is(false));
    }

    @Test
    public void testInValidName() {

        Variable variable = new Variable(null, null, null, "script");
        ValidationResult result = validator.validate(variable);
        assertThat(result, notNullValue());
        assertThat(result.isValid(), is(false));
    }
}
