package com.github.ricardocomar.camunda.mockservice.handler.helper;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import com.github.ricardocomar.camunda.mockservice.handler.exception.ServiceFailureException;
import com.github.ricardocomar.camunda.mockservice.model.Scenario;
import com.github.ricardocomar.camunda.mockservice.model.Variable;
import org.camunda.bpm.client.task.ExternalTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.micrometer.core.instrument.util.StringUtils;

public class VariableHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(VariableHelper.class);

    final ScriptHelper scriptHelper = new ScriptHelper();

    public Map<String, Object> handleVariables(ExternalTask externalTask, Scenario scenario)
            throws ServiceFailureException {

        Map<String, Object> localVariables = new HashMap<>();
        Map<String, Object> errors = new HashMap<>();
        localVariables.putAll(scenario.getVariables().stream()
                .collect(Collectors.toMap(Variable::getName, variable -> {
                    return handleVariable(externalTask, localVariables, errors, variable);
                })));

        if (!errors.isEmpty()) {
            throw new ServiceFailureException("Scenario with variable error",
                    "SCENARIO_VARIABLE_INVALID",
                    "Mock Scenario with invalid variables definition for topic "
                            + externalTask.getTopicName() + " and Scenario-Id "
                            + scenario.getScenarioId() + " - " + errors,
                    0, 0L);
        }
        return localVariables;
    }

    public final Object handleVariable(ExternalTask externalTask, Map<String, Object> variables,
            Map<String, Object> errors, Variable variable) {

        try {
            if (StringUtils.isNotEmpty(variable.getGroovyScript())) {
                return scriptHelper.evalScript(variable.getGroovyScript(),
                        externalTask.getAllVariables(), variables);
            }
            return Class.forName(variable.getClassName()).getConstructor(String.class)
                    .newInstance(variable.getValue());
        } catch (Exception e) {
            LOGGER.error("Variable [[[" + variable + "]]] invalid");
            errors.put(variable.getName(), e.getMessage());
            return null;
        }

    }


}
