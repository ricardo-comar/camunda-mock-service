package com.github.ricardocomar.camunda.mockservice.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import com.github.ricardocomar.camunda.mockservice.MockServiceApplication;
import com.github.ricardocomar.camunda.mockservice.model.ScenarioError;
import org.junit.Test;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

public class ErrorValidatorTest {

    private ErrorValidator validator = new ErrorValidator();

    public ErrorValidatorTest() {
        FixtureFactoryLoader.loadTemplates(MockServiceApplication.class.getPackage().getName());
    }

    @Test
    public void testValid() {
        ScenarioError error = Fixture.from(ScenarioError.class).gimme("valid");
        assertThat(validator.validate(error).isValid(), is(true));
        assertThat(validator.validate(new ScenarioError("asdasd", "aaa")).isValid(), is(true));
    }

    @Test
    public void testInvalid() {

        assertThat(validator.validate(new ScenarioError(null, null)).isValid(), is(false));
        assertThat(validator.validate(new ScenarioError(null, "aaa")).isValid(), is(false));
        assertThat(validator.validate(new ScenarioError("asdasd", null)).isValid(), is(false));
    }

}
