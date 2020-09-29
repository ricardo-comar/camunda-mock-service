package com.github.ricardocomar.camunda.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import java.util.Collections;
import com.github.ricardocomar.camunda.mockservice.MockServiceApplication;
import com.github.ricardocomar.camunda.mockservice.model.Scenario;
import com.github.ricardocomar.camunda.mockservice.model.ScenarioError;
import com.github.ricardocomar.camunda.mockservice.model.ScenarioFailure;
import com.github.ricardocomar.camunda.mockservice.model.Variable;
import com.github.ricardocomar.camunda.mockservice.validator.ConditionValidator;
import com.github.ricardocomar.camunda.mockservice.validator.DelayValidator;
import com.github.ricardocomar.camunda.mockservice.validator.ErrorValidator;
import com.github.ricardocomar.camunda.mockservice.validator.FailureValidator;
import com.github.ricardocomar.camunda.mockservice.validator.ScenarioValidator;
import com.github.ricardocomar.camunda.mockservice.validator.VariableValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ScenarioValidatorTest {

    @InjectMocks
    private ScenarioValidator validator = new ScenarioValidator();

    @Spy
    private VariableValidator varValidator = new VariableValidator();

    @Spy
    private ConditionValidator condValidator = new ConditionValidator();

    @Spy
    private DelayValidator delayValidator = new  DelayValidator();

    @Spy
    private FailureValidator failureValidator = new FailureValidator();

    @Spy
    private ErrorValidator errorValidator = new ErrorValidator();

    private Scenario scenario;

    public ScenarioValidatorTest() {
        FixtureFactoryLoader.loadTemplates(MockServiceApplication.class.getPackage().getName());
    }

    @Test
    public void testValid() {

        scenario = Fixture.from(Scenario.class).gimme("valid");
        assertThat(validator.validate(scenario).isValid(), is(true));
    }

    @Test
    public void testMandatory() {

        scenario  = Fixture.from(Scenario.class).gimme("valid");
        scenario.setTopicName(null);
        assertThat(validator.validate(scenario).isValid(), is(false));

        scenario  = Fixture.from(Scenario.class).gimme("valid");
        scenario.setPriority(null);
        assertThat(validator.validate(scenario).isValid(), is(false));

        scenario  = Fixture.from(Scenario.class).gimme("valid");
        scenario.setCondition(null);
        assertThat(validator.validate(scenario).isValid(), is(false));

        scenario  = Fixture.from(Scenario.class).gimme("valid");
        scenario.setVariables(null);
        assertThat(validator.validate(scenario).isValid(), is(false));

        scenario  = Fixture.from(Scenario.class).gimme("valid");
        scenario.setVariables(Collections.emptyList());
        assertThat(validator.validate(scenario).isValid(), is(false));

    }

    @Test
    public void testInvalidCombination() {

        scenario  = Fixture.from(Scenario.class).gimme("valid");
        scenario.setVariables(Fixture.from(Variable.class).gimme(2, "script", "string"));
        scenario.setFailure(Fixture.from(ScenarioFailure.class).gimme("valid"));
        scenario.setError(Fixture.from(ScenarioError.class).gimme("valid"));
        assertThat(validator.validate(scenario).isValid(), is(false));

        scenario.setVariables(null);
        scenario.setFailure(null);
        scenario.setError(null);
        assertThat(validator.validate(scenario).isValid(), is(false));

        scenario.setVariables(null);
        scenario.setFailure(Fixture.from(ScenarioFailure.class).gimme("valid"));
        scenario.setError(Fixture.from(ScenarioError.class).gimme("valid"));
        assertThat(validator.validate(scenario).isValid(), is(false));

        scenario.setVariables(Fixture.from(Variable.class).gimme(2, "script", "string"));
        scenario.setFailure(Fixture.from(ScenarioFailure.class).gimme("valid"));
        scenario.setError(null);
        assertThat(validator.validate(scenario).isValid(), is(false));

        scenario.setVariables(Fixture.from(Variable.class).gimme(2, "script", "string"));
        scenario.setFailure(null);
        scenario.setError(Fixture.from(ScenarioError.class).gimme("valid"));
        assertThat(validator.validate(scenario).isValid(), is(false));
    }
}
