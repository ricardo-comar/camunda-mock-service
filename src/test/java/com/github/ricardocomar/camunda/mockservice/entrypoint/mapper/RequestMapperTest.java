package com.github.ricardocomar.camunda.mockservice.entrypoint.mapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import com.github.ricardocomar.camunda.mockservice.MockServiceApplication;
import com.github.ricardocomar.camunda.mockservice.entrypoint.model.ScenarioRequest;
import com.github.ricardocomar.camunda.mockservice.model.Scenario;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;


@RunWith(MockitoJUnitRunner.Silent.class)
public class RequestMapperTest {

    @InjectMocks
    ScenarioRequestMapper mapper = new ScenarioRequestMapperImpl();

    @Spy
    VariableRequestMapper variableRequestMapper = new VariableRequestMapperImpl();

    @Spy
    ConditionRequestMapper conditionRequestMapper = new ConditionRequestMapperImpl();

    @Spy
    DelayRequestMapper delayRequestMapper = new DelayRequestMapperImpl();

    @Spy
    FailureRequestMapper failureRequestMapper = new FailureRequestMapperImpl();

    public RequestMapperTest() {
        FixtureFactoryLoader.loadTemplates(MockServiceApplication.class.getPackage().getName());
    }

    @Test
    public void testRequestToModel() {
        assertThat(Fixture.from(Scenario.class).gimme("valid"),
                equalTo(mapper.fromRequest(Fixture.from(ScenarioRequest.class).gimme("valid"))));

        assertThat(Fixture.from(Scenario.class).gimme("valid-failure"),
                equalTo(mapper.fromRequest(Fixture.from(ScenarioRequest.class).gimme("valid-failure"))));
    }

}
