package com.github.ricardocomar.camunda.mockservice.handler.helper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.github.ricardocomar.camunda.mockservice.handler.exception.ServiceFailureException;
import com.github.ricardocomar.camunda.mockservice.model.Condition;
import com.github.ricardocomar.camunda.mockservice.model.Scenario;
import com.github.ricardocomar.camunda.mockservice.model.Variable;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class VariableHelperTest {

    private Map<String, Object> taskVariables;
    private Map<String, Object> resultVariables;

    private ExternalTaskService externalTaskService;
    private ExternalTask externalTask;

    private List<Variable> returnVariables = new ArrayList<>();
    private final Condition condition = new Condition("return true");
    private Scenario scenario = new Scenario("scenarioId", "mockTopic", condition, 1, null, null, null,
                returnVariables);

    @Before
    public void before() {
        resultVariables = new HashMap<>();
        taskVariables = new HashMap<>();
        taskVariables.put("myVar", 4);
        
        returnVariables.clear();

        externalTaskService = Mockito.mock(ExternalTaskService.class);
        externalTask = Mockito.mock(ExternalTask.class);

        Mockito.when(externalTask.getAllVariables()).thenReturn(taskVariables);
        Mockito.when(externalTask.getTopicName()).thenReturn("mockScenario");

        Mockito.doNothing().when(externalTaskService).handleFailure(eq(externalTask), anyString(),
                anyString(), anyInt(), anyLong());
        Mockito.doAnswer(inv -> {
            resultVariables.putAll(inv.getArgument(1));;
            return null;
        }).when(externalTaskService).complete(eq(externalTask), any());

    }

    @Test
    public void testHandleVariablePrimitiveValid() throws ServiceFailureException {

        returnVariables.add(new Variable("variable", "15", "java.lang.Long", null));

        Map<String, Object> result = VariableHelper.handleVariables(externalTask, scenario);

        assertThat(result.values(), hasSize(1));
        assertThat(result.get("variable"), equalTo(15L));
    }

    @Test
    public void testHandleVariableScriptValid() throws ServiceFailureException {

        returnVariables.add(new Variable("variable", null, null, "return myVar * 3"));

        Map<String, Object> result = VariableHelper.handleVariables(externalTask, scenario);

        assertThat(result.values(), hasSize(1));
        assertThat(result.get("variable"), equalTo(12));
    }

    @Test(expected = ServiceFailureException.class)
    public void testHandleVariableInvalid() throws ServiceFailureException {

        returnVariables.add(new Variable("variable", "15", "dasdasdasdLong", null));

        VariableHelper.handleVariables(externalTask, scenario);
        fail("Should have thrown ServiceFailureException");
    }

    @Test(expected = ServiceFailureException.class)
    public void testHandleScriptInvalid() throws ServiceFailureException {

        returnVariables.add(new Variable("variable", null, null, "asdas rrs myVar * 3"));

        VariableHelper.handleVariables(externalTask, scenario);
        fail("Should have thrown ServiceFailureException");
    }

}
