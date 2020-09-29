package com.github.ricardocomar.camunda.mockservice.handler.helper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.github.ricardocomar.camunda.mockservice.MockServiceApplication;
import com.github.ricardocomar.camunda.mockservice.handler.exception.ServiceFailureException;
import com.github.ricardocomar.camunda.mockservice.model.Condition;
import com.github.ricardocomar.camunda.mockservice.model.Scenario;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

public class ScenarioHelperTest {

    private Map<String, Object> taskVariables;
    private Map<String, Object> resultVariables;

    private ExternalTaskService externalTaskService;
    private ExternalTask externalTask;

    private final String topicName = "mockTopic";

    public ScenarioHelperTest() {
        FixtureFactoryLoader.loadTemplates(MockServiceApplication.class.getPackage().getName());
    }

    @Before
    public void before() {
        resultVariables = new HashMap<>();
        taskVariables = new HashMap<>();
        taskVariables.put("myVar", 4);

        externalTaskService = Mockito.mock(ExternalTaskService.class);
        externalTask = Mockito.mock(ExternalTask.class);

        Mockito.when(externalTask.getAllVariables()).thenReturn(taskVariables);
        Mockito.when(externalTask.getTopicName()).thenReturn(topicName);

        Mockito.doNothing().when(externalTaskService).handleFailure(eq(externalTask), anyString(),
                anyString(), anyInt(), anyLong());
        Mockito.doAnswer(inv -> {
            resultVariables.putAll(inv.getArgument(1));;
            return null;
        }).when(externalTaskService).complete(eq(externalTask), any());

    }

    @Test
    public void testHandleConditionSimple() {

        Condition condition = new Condition("return true");
        Object result = ScenarioHelper.handleCondition(externalTask, condition);

        assertThat(result, equalTo(true));
    }

    @Test
    public void testHandleConditionWithVariable() {

        taskVariables.put("myVar", 5);
        Condition condition = new Condition("return myVar > 4");
        Object result = ScenarioHelper.handleCondition(externalTask, condition);

        assertThat(result, equalTo(true));
    }

    @Test
    public void testHandleConditionNotBoolean() {

        Condition condition = new Condition("return 10");
        Object result = ScenarioHelper.handleCondition(externalTask, condition);

        assertThat(result, equalTo(false));
    }

    @Test(expected = ServiceFailureException.class)
    public void testExtractScenarioNullList() throws ServiceFailureException {

        ScenarioHelper.extractScenario(externalTask, topicName, null);

    }

    @Test(expected = ServiceFailureException.class)
    public void testExtractScenarioEmptyList() throws ServiceFailureException {

        ScenarioHelper.extractScenario(externalTask, topicName, Collections.emptyList());
    }

    @Test(expected = ServiceFailureException.class)
    public void testExtractScenarioFalse() throws ServiceFailureException {

        List<Scenario> scenarios = Arrays.asList(new Scenario("scenarioId", "mockTopic",
                new Condition("return false"), 1, null, null, null, new ArrayList<>()));
        ScenarioHelper.extractScenario(externalTask, topicName, scenarios);
    }

    @Test
    public void testExtractScenarioPriority() throws ServiceFailureException {

        List<Scenario> scenarios = Arrays.asList(Fixture.from(Scenario.class).gimme("valid-1"),
                Fixture.from(Scenario.class).gimme("valid-2"));

        Scenario returned = ScenarioHelper.extractScenario(externalTask, topicName, scenarios);
        assertThat(returned, equalTo(Fixture.from(Scenario.class).gimme("valid-1")));
    }

}
