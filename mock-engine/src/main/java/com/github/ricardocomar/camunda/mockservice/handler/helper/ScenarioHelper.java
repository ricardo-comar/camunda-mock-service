package com.github.ricardocomar.camunda.mockservice.handler.helper;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import com.github.ricardocomar.camunda.mockservice.handler.exception.ServiceFailureException;
import com.github.ricardocomar.camunda.mockservice.model.Condition;
import com.github.ricardocomar.camunda.mockservice.model.Scenario;
import org.camunda.bpm.client.task.ExternalTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScenarioHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScenarioHelper.class);

    private ScenarioHelper() {
    }

    public static Scenario extractScenario(ExternalTask externalTask, String topicName,
            List<Scenario> scenarios) throws ServiceFailureException {

        Optional.ofNullable(scenarios)
                .orElseThrow(() -> new ServiceFailureException(
                        "Empty scenario list for topic " + topicName, "SCENARIO_ABSENT",
                        "Empty scenario list for topic " + topicName, 0, 0L));

        scenarios.stream().findFirst()
                .orElseThrow(() -> new ServiceFailureException(
                        "No Scenario found in database for topic " + topicName, "SCENARIO_ABSENT",
                        "Mock Scenario not found for topic " + topicName, 0, 0L));

        Set<Scenario> matchScenarios =
                scenarios.stream().filter(s -> handleCondition(externalTask, s.getCondition()))
                        .collect(Collectors.toSet());

        matchScenarios.stream().findFirst()
                .orElseThrow(() -> new ServiceFailureException(
                        "No matching condition found for topic " + topicName,
                        "MATCHING_CONDITION_ABSENT",
                        "No matching condition found for topic " + topicName, 0, 0L));

        Scenario scenario =
                matchScenarios.stream().sorted(Comparator.comparingLong(Scenario::getPriority))
                        .collect(Collectors.toList()).stream().findFirst().get();
        return scenario;
    }

    public static boolean handleCondition(ExternalTask externalTask, Condition condition) {
        try {
            return (Boolean) ScriptHelper.evalScript(condition.getConditionScript(),
                    externalTask.getAllVariables());

        } catch (Exception e) {
            LOGGER.error("Condition [[[" + condition + "]]] groovy script invalid");
            return false;
        }

    }



}
