package com.github.ricardocomar.camunda.mockservice.handler;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.github.ricardocomar.camunda.mockservice.MockServiceApplication;
import com.github.ricardocomar.camunda.mockservice.model.Condition;
import com.github.ricardocomar.camunda.mockservice.model.Delay;
import com.github.ricardocomar.camunda.mockservice.model.Delay;
import com.github.ricardocomar.camunda.mockservice.model.Scenario;
import com.github.ricardocomar.camunda.mockservice.model.Variable;
import com.github.ricardocomar.camunda.mockservice.usecase.QueryScenarioUseCase;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;


@RunWith(MockitoJUnitRunner.Silent.class)
public class MockServiceHandlerTest {

    @InjectMocks
    private MockServiceHandler handler = new MockServiceHandler();

    @Mock
    private QueryScenarioUseCase queryScenario;

    @Mock
    private ExternalTask externalTask;

    @Mock
    private ExternalTaskService externalTaskService;

    private Map<String, Object> taskVariables;
    private Map<String, Object> variables;
    private Map<String, Object> errors;
    private Map<String, Object> resultVariables;

    private List<Scenario> scenarios;

    public MockServiceHandlerTest() {
        FixtureFactoryLoader.loadTemplates(MockServiceApplication.class.getPackage().getName());
    }

    @Before
    public void before() {
        taskVariables = new HashMap<>();
        variables = new HashMap<>();
        errors = new HashMap<>();
        scenarios = new ArrayList<>();
        resultVariables = new HashMap<>();

        Mockito.when(externalTask.getAllVariables()).thenReturn(taskVariables);
        Mockito.when(externalTask.getTopicName()).thenReturn("mockScenario");
        Mockito.when(queryScenario.queryScenarios(eq("mockScenario"))).thenReturn(scenarios);

        Mockito.doNothing().when(externalTaskService).handleFailure(eq(externalTask), anyString(),
                anyString(), anyInt(), anyLong());
        Mockito.doAnswer(inv -> {
            resultVariables.putAll(inv.getArgument(1));;
            return null;
        }).when(externalTaskService).complete(eq(externalTask), any());
    }

    @Test
    public void testValidRegisterTopic() {
        String topicName = "mockTopic";

        assertThat(handler.isTopicRegistred(topicName), equalTo(false));
        
        handler.registerTopic(topicName);
        assertThat(handler.isTopicRegistred(topicName), equalTo(true));
        
        handler.removeTopic(topicName);
        assertThat(handler.isTopicRegistred(topicName), equalTo(false));
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidRegisterTopic() {
        String topicName = "mockTopic";
        handler.registerTopic(topicName);
        handler.registerTopic(topicName);
    }
    
    @Test
    public void testScriptValid() throws Exception {

        variables.put("myVar", 4);
        Object result = handler.evalScript("return 7 * myVar", variables);

        assertThat(errors.values(), hasSize(0));
        assertThat(variables.values(), hasSize(1));
        assertThat(result, equalTo(28));
    }

    @Test(expected = Exception.class)
    @Ignore("Turn on when identify how to throw exception on runtime errors")
    public void testScriptError() throws Exception {

        variables.put("myVarX", 4);
        Object result = handler.evalScript("return 7 * myVar", variables);

        assertThat(errors.values(), hasSize(1));
        assertThat(variables.values(), hasSize(1));
        assertThat(result, nullValue());
    }

    @Test
    public void testHandleVariableValid() {

        variables.put("myVar", 4);
        Variable variable = new Variable("variable", "15", "java.lang.Long", null);
        Object result = handler.handleVariable(externalTask, variables, errors, variable);

        assertThat(errors.values(), hasSize(0));
        assertThat(variables.values(), hasSize(1));
        assertThat(result, equalTo(15L));
    }

    @Test
    public void testHandleDelay() {

        handler.handleDelay(null);
        handler.handleDelay(new Delay(100, null, null));
        handler.handleDelay(new Delay(null, 100, 200));
    }
    
    @Test
    public void testHandleVariableInvalid() {

        variables.put("myVar", 4);
        Variable variable = new Variable("variable", "15", "xxx", null);
        Object result = handler.handleVariable(externalTask, variables, errors, variable);

        assertThat(errors.values(), hasSize(1));
        assertThat(variables.values(), hasSize(1));
        assertThat(result, nullValue());
    }

    @Test
    public void testHandleConditionSimple() {

        Condition condition = new Condition("return true");
        Object result = handler.handleCondition(externalTask, condition);

        assertThat(result, equalTo(true));
    }

    @Test
    public void testHandleConditionWithVariable() {

        taskVariables.put("myVar", 5);
        Condition condition = new Condition("return myVar > 4");
        Object result = handler.handleCondition(externalTask, condition);

        assertThat(result, equalTo(true));
    }

    @Test
    public void testHandleConditionNotBoolean() {

        Condition condition = new Condition("return 10");
        Object result = handler.handleCondition(externalTask, condition);

        assertThat(result, equalTo(false));
    }

    @Test
    public void testExecuteEmptyScenarios() {

        handler.execute(externalTask, externalTaskService);
        assertThat(resultVariables.entrySet(), hasSize(0));

        Mockito.verify(externalTaskService, Mockito.only()).handleFailure(eq(externalTask),
                eq("SCENARIO_ABSENT"), anyString(), anyInt(), anyLong());
        Mockito.verify(externalTaskService, Mockito.never()).complete(any(), any());
    }

    @Test
    public void testExecuteSingleFalseScenario() {

        scenarios.add(Fixture.from(Scenario.class).gimme("valid-falseCondition"));

        handler.execute(externalTask, externalTaskService);
        assertThat(resultVariables.entrySet(), hasSize(0));

        Mockito.verify(externalTaskService, Mockito.only()).handleFailure(eq(externalTask),
                eq("MATCHING_CONDITION_ABSENT"), anyString(), anyInt(), anyLong());
        Mockito.verify(externalTaskService, Mockito.never()).complete(any(), any());
    }

    @Test
    public void testExecuteOrderedScenario() {

        scenarios.add(Fixture.from(Scenario.class).gimme("valid-1"));
        scenarios.add(Fixture.from(Scenario.class).gimme("valid-2"));

        handler.execute(externalTask, externalTaskService);
        assertThat(resultVariables.entrySet(), hasSize(1));
        assertThat(resultVariables, hasKey("longVariable"));

        Mockito.verify(externalTaskService, Mockito.never()).handleFailure(eq(externalTask),
                anyString(), anyString(), anyInt(), anyLong());
        Mockito.verify(externalTaskService, Mockito.only()).complete(eq(externalTask), any());
    }

    @Test
    public void testExecuteTrueAndFalseScenario() {

        scenarios.add(Fixture.from(Scenario.class).gimme("valid-1"));
        scenarios.add(Fixture.from(Scenario.class).gimme("valid-falseCondition"));

        handler.execute(externalTask, externalTaskService);
        assertThat(resultVariables.entrySet(), hasSize(1));
        assertThat(resultVariables, hasKey("longVariable"));

        Mockito.verify(externalTaskService, Mockito.never()).handleFailure(eq(externalTask),
                anyString(), anyString(), anyInt(), anyLong());
        Mockito.verify(externalTaskService, Mockito.only()).complete(eq(externalTask), any());
    }

}
