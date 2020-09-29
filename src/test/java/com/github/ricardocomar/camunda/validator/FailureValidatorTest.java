package com.github.ricardocomar.camunda.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import com.github.ricardocomar.camunda.mockservice.MockServiceApplication;
import com.github.ricardocomar.camunda.mockservice.model.ScenarioFailure;
import com.github.ricardocomar.camunda.mockservice.validator.FailureValidator;
import org.junit.Test;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

public class FailureValidatorTest {

    private FailureValidator validator = new FailureValidator();

    public FailureValidatorTest() {
        FixtureFactoryLoader.loadTemplates(MockServiceApplication.class.getPackage().getName());
    }

    @Test
    public void testValid() {
        ScenarioFailure failure = Fixture.from(ScenarioFailure.class).gimme("valid");
        assertThat(validator.validate(failure).isValid(), is(true));
        assertThat(validator.validate(new ScenarioFailure("asdasd", "aaa", null, null)).isValid(), is(true));
    }

    @Test
    public void testInvalid() {

        assertThat(validator.validate(new ScenarioFailure(null, null, null, null)).isValid(), is(false));
        assertThat(validator.validate(new ScenarioFailure(null, null, null, 1000L)).isValid(), is(false));
        assertThat(validator.validate(new ScenarioFailure(null, null, 300, 1000L)).isValid(), is(false));
        assertThat(validator.validate(new ScenarioFailure(null, "aaa", 300, 1000L)).isValid(), is(false));
        assertThat(validator.validate(new ScenarioFailure("asdasd", null, null, null)).isValid(), is(false));
        assertThat(validator.validate(new ScenarioFailure("asdasd", "aaa", -10, null)).isValid(), is(false));
        assertThat(validator.validate(new ScenarioFailure("asdasd", "aaa", null, -100L)).isValid(), is(false));
    }

}
