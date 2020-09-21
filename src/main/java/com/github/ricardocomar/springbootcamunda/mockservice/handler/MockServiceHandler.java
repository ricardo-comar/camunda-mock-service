package com.github.ricardocomar.springbootcamunda.mockservice.handler;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import com.github.ricardocomar.springbootcamunda.mockservice.model.Condition;
import com.github.ricardocomar.springbootcamunda.mockservice.model.Scenario;
import com.github.ricardocomar.springbootcamunda.mockservice.model.Variable;
import com.github.ricardocomar.springbootcamunda.mockservice.usecase.QueryScenarioUseCase;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

@Component
public class MockServiceHandler implements ExternalTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalTaskHandler.class);

    @Autowired
    private QueryScenarioUseCase queryScenario;

    private Set<String> topicsRegistred = Collections.synchronizedSet(new HashSet<String>());

    public void registerTopic(String topic) {
        if (topicsRegistred.contains(topic)) {
            throw new RuntimeException("Topic already registred");
        }
        topicsRegistred.add(topic);
    }

    public void removeTopic(String topic) {
        if (!topicsRegistred.contains(topic)) {
            throw new RuntimeException("Topic not registred");
        }
        topicsRegistred.remove(topic);
    }

    public boolean isTopicRegistred(String topic) {
        return topicsRegistred.contains(topic);
    }

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {

        String topicName = externalTask.getTopicName();

        List<Scenario> scenarios = queryScenario.queryScenarios(topicName);

        if (scenarios.isEmpty()) {
            LOGGER.error("No Scenario found for topic {} not found in database", topicName);
            externalTaskService.handleFailure(externalTask, "SCENARIO_ABSENT",
                    "Mock Scenario not found for topic " + topicName, 0, 0);
        }

        Set<Scenario> matchScenarios = new HashSet<Scenario>();
        for (Scenario scenario : scenarios) {
            if (handleCondition(externalTask, scenario.getCondition())) {
                matchScenarios.add(scenario);
            }
        }
        if (matchScenarios.isEmpty()) {
            LOGGER.error("No matching condition found for topic {} scenarios", topicName);
            externalTaskService.handleFailure(externalTask, "MATCHING_CONDITION_ABSENT",
                    "No matching condition found for topic " + topicName, 0, 0);
        }

        Optional<Scenario> scenarioOpt =
                matchScenarios.stream().sorted(Comparator.comparingLong(Scenario::getOrder))
                        .collect(Collectors.toList()).stream().findFirst();

        Map<String, Object> variables = new HashMap<>();
        Map<String, Object> errors = new HashMap<>();
        variables.putAll(scenarioOpt.get().getVariables().stream()
                .collect(Collectors.toMap(Variable::getName, variable -> {
                    return handleVariable(externalTask, variables, errors, variable);
                })));

        if (!errors.isEmpty()) {
            externalTaskService.handleFailure(externalTask, "SCENARIO_VARIABLE_INVALID",
                    "Mock Scenario with invalid variables definition for topic " + topicName
                            + " and Scenario-Id " + scenarioOpt.get().getScenarioId() + " - "
                            + errors,
                    0, 0);
        }

        LOGGER.info("Execution completed with variables [[[{}]]]", variables);
        externalTaskService.complete(externalTask, variables);
    }

    private boolean handleCondition(ExternalTask externalTask, Condition condition) {
        try {
            return (Boolean) evalScript(condition.getConditionScript(),
                    externalTask.getAllVariables());

        } catch (Exception e) {
            LOGGER.error("Condition [[[" + condition + "]]] groovy script invalid");
            return false;
        }

    }

    protected Object evalScript(String script, Map<String, Object>... envVars) throws Exception {

        Binding binding = new Binding();
        Arrays.asList(envVars).stream()
                .forEach(e -> e.forEach((k, v) -> binding.setProperty(k, v)));

        return new GroovyShell(binding).evaluate(script);
    }

    protected Object handleVariable(ExternalTask externalTask, Map<String, Object> variables,
            Map<String, Object> errors, Variable variable) {
        if (StringUtils.isNotEmpty(variable.getGroovyScript())) {

            try {
                return evalScript(variable.getGroovyScript(), externalTask.getAllVariables(),
                        variables);
            } catch (Exception e) {
                LOGGER.error("Variable [[[" + variable + "]]] groovy script invalid");
                errors.put(variable.getName(), e.getMessage());
                return null;
            }

        } else {
            try {
                return Class.forName(variable.getClassName()).getConstructor(String.class)
                        .newInstance(variable.getValue());
            } catch (Exception e) {
                LOGGER.error("Variable [[[" + variable + "]]] invalid");
                errors.put(variable.getName(), e.getMessage());
                return e.getMessage();
            }

        }
    }


}
