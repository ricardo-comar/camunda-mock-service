package com.github.ricardocomar.camunda.mockservice.handler;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.github.ricardocomar.camunda.mockservice.MockServiceApplication;
import com.github.ricardocomar.camunda.mockservice.handler.helper.ScriptHelper;
import com.github.ricardocomar.camunda.mockservice.model.Condition;
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
    private Map<String, Object> resultVariables;
    private List<Scenario> scenarios;
    private final String topicName = "mockTopic";

    public MockServiceHandlerTest() {
        FixtureFactoryLoader.loadTemplates(MockServiceApplication.class.getPackage().getName());
    }

    @Before
    public void before() {
        taskVariables = new HashMap<>();
        scenarios = new ArrayList<>();
        resultVariables = new HashMap<>();

        Mockito.when(externalTask.getAllVariables()).thenReturn(taskVariables);
        Mockito.when(externalTask.getTopicName()).thenReturn(topicName);
        Mockito.when(queryScenario.queryScenarios(eq(topicName))).thenReturn(scenarios);

        Mockito.doNothing().when(externalTaskService).handleFailure(eq(externalTask), anyString(),
                anyString(), anyInt(), anyLong());
        Mockito.doAnswer(inv -> {
            resultVariables.putAll(inv.getArgument(1));;
            return null;
        }).when(externalTaskService).complete(eq(externalTask), any());
    }

    @Test
    public void testValidRegisterTopic() {

        assertThat(handler.isTopicRegistred(topicName), equalTo(false));
        
        handler.registerTopic(topicName);
        assertThat(handler.isTopicRegistred(topicName), equalTo(true));
        
        handler.removeTopic(topicName);
        assertThat(handler.isTopicRegistred(topicName), equalTo(false));
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

    @Test
    public void testDesiredFailureCondition() {

        scenarios.add(Fixture.from(Scenario.class).gimme("valid-failure"));

        handler.execute(externalTask, externalTaskService);
        assertThat(resultVariables.entrySet(), hasSize(0));

        Mockito.verify(externalTaskService, Mockito.only()).handleFailure(eq(externalTask),
                eq("FAILURE_FIXTURE"), eq("Expected Failure"), eq(3), eq(1000L));
        Mockito.verify(externalTaskService, Mockito.never()).complete(eq(externalTask), any());
    }

}
