package com.github.ricardocomar.camunda.mockservice.handler;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import com.github.ricardocomar.camunda.mockservice.model.Condition;
import com.github.ricardocomar.camunda.mockservice.model.Delay;
import com.github.ricardocomar.camunda.mockservice.model.Failure;
import com.github.ricardocomar.camunda.mockservice.model.Scenario;
import com.github.ricardocomar.camunda.mockservice.model.Variable;
import com.github.ricardocomar.camunda.mockservice.usecase.QueryScenarioUseCase;
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
            LOGGER.error("No Scenario found in database for topic {}", topicName);
            externalTaskService.handleFailure(externalTask, "SCENARIO_ABSENT",
                    "Mock Scenario not found for topic " + topicName, 0, 0);
            return;
        }

        Set<Scenario> matchScenarios =
                scenarios.stream().filter(s -> handleCondition(externalTask, s.getCondition()))
                        .collect(Collectors.toSet());

        if (matchScenarios.isEmpty()) {
            LOGGER.error("No matching condition found for topic {} scenarios", topicName);
            externalTaskService.handleFailure(externalTask, "MATCHING_CONDITION_ABSENT",
                    "No matching condition found for topic " + topicName, 0, 0);
            return;
        }

        Scenario scenario =
                matchScenarios.stream().sorted(Comparator.comparingLong(Scenario::getPriority))
                        .collect(Collectors.toList()).stream().findFirst().get();

        handleDelay(scenario.getDelay());


        if (scenario.getFailure() != null) {
            Failure failure = scenario.getFailure();
            LOGGER.info("Scenario with expected failure :{} ", failure.getMessage());
            externalTaskService.handleFailure(externalTask, failure.getMessage(),
                    failure.getDetails(), Optional.of(failure.getRetryTimes()).orElse(0),
                    Optional.of(failure.getRetryTimeout()).orElse(0L));
            return;
        }


        Map<String, Object> handlerVariables = new HashMap<>();
        Map<String, Object> errors = new HashMap<>();
        handlerVariables.putAll(scenario.getVariables().stream()
                .collect(Collectors.toMap(Variable::getName, variable -> {
                    return handleVariable(externalTask, handlerVariables, errors, variable);
                })));

        if (!errors.isEmpty()) {
            externalTaskService.handleFailure(externalTask, "SCENARIO_VARIABLE_INVALID",
                    "Mock Scenario with invalid variables definition for topic " + topicName
                            + " and Scenario-Id " + scenario.getScenarioId() + " - " + errors,
                    0, 0);
            return;
        }

        LOGGER.info("Execution completed with variables [[[{}]]]", handlerVariables);
        externalTaskService.complete(externalTask, handlerVariables);
    }

    protected void handleDelay(Delay delay) {

        if (delay != null) {
            Integer wait = Optional.ofNullable(delay.getFixedMs()).orElseGet(
                    () -> ThreadLocalRandom.current().nextInt(delay.getMinMs(), delay.getMaxMs()));
            LOGGER.info("Execution will sleep for {}ms...", wait);

            try {
                TimeUnit.MILLISECONDS.sleep(wait);
            } catch (InterruptedException e) {
                LOGGER.info("Sleep finished ! Execution will carry on");
            }

        }
    }

    protected boolean handleCondition(ExternalTask externalTask, Condition condition) {
        try {
            return (Boolean) evalScript(condition.getConditionScript(),
                    externalTask.getAllVariables());

        } catch (Exception e) {
            LOGGER.error("Condition [[[" + condition + "]]] groovy script invalid");
            return false;
        }

    }

    @SafeVarargs
    protected final Object evalScript(String script, Map<String, Object>... envVars)
            throws Exception {

        Binding binding = new Binding();
        Arrays.asList(envVars).stream()
                .forEach(e -> e.forEach((k, v) -> binding.setProperty(k, v)));

        return new GroovyShell(binding).evaluate(script);
    }

    protected Object handleVariable(ExternalTask externalTask, Map<String, Object> variables,
            Map<String, Object> errors, Variable variable) {

        try {
            if (StringUtils.isNotEmpty(variable.getGroovyScript())) {
                return evalScript(variable.getGroovyScript(), externalTask.getAllVariables(),
                        variables);
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
