package com.github.ricardocomar.camunda.mockservice.entrypoint.mapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import com.github.ricardocomar.camunda.mockservice.MockServiceApplication;
import com.github.ricardocomar.camunda.mockservice.entrypoint.model.ScenarioRequest;
import com.github.ricardocomar.camunda.mockservice.model.Scenario;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;


public class RequestMapperTest {

    ScenarioRequestMapper mapper = new ScenarioRequestMapperImpl();
    private Scenario model = Fixture.from(Scenario.class).gimme("valid");
    private ScenarioRequest request = Fixture.from(ScenarioRequest.class).gimme("valid");

    public RequestMapperTest() {
        ReflectionTestUtils.setField(mapper, "variableRequestMapper",
                new VariableRequestMapperImpl());
        ReflectionTestUtils.setField(mapper, "conditionRequestMapper",
                new ConditionRequestMapperImpl());
    }

    @BeforeClass
    public static void beforeClass() {
        FixtureFactoryLoader.loadTemplates(MockServiceApplication.class.getPackage().getName());
    }

    @Test
    public void testRequestToModel() {
        model.setScenarioId(null);
        assertThat(model, equalTo(mapper.fromRequest(request)));
    }

}
