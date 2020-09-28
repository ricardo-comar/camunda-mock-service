package com.github.ricardocomar.camunda.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import java.util.Collections;
import com.github.ricardocomar.camunda.mockservice.MockServiceApplication;
import com.github.ricardocomar.camunda.mockservice.model.Scenario;
import com.github.ricardocomar.camunda.mockservice.validator.ConditionValidator;
import com.github.ricardocomar.camunda.mockservice.validator.DelayValidator;
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
    public void testEmpty() {

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


}
