package com.github.ricardocomar.camunda.mockservice.handler;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.github.ricardocomar.camunda.mockservice.handler.exception.ServiceErrorException;
import com.github.ricardocomar.camunda.mockservice.handler.exception.ServiceFailureException;
import com.github.ricardocomar.camunda.mockservice.handler.helper.DelayHelper;
import com.github.ricardocomar.camunda.mockservice.handler.helper.ExceptionHelper;
import com.github.ricardocomar.camunda.mockservice.handler.helper.ScenarioHelper;
import com.github.ricardocomar.camunda.mockservice.handler.helper.TopicHelper;
import com.github.ricardocomar.camunda.mockservice.handler.helper.VariableHelper;
import com.github.ricardocomar.camunda.mockservice.model.Scenario;
import com.github.ricardocomar.camunda.mockservice.usecase.QueryScenarioUseCase;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MockServiceHandler implements ExternalTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalTaskHandler.class);

    final TopicHelper topicHelper = new TopicHelper();

    @Autowired
    private QueryScenarioUseCase queryScenario;

    public void registerTopic(String topic) {
        topicHelper.registerTopic(topic);
    }

    public void removeTopic(String topic) {
        topicHelper.removeTopic(topic);
    }

    public boolean isTopicRegistred(String topic) {
        return topicHelper.isTopicRegistred(topic);
    }

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {

        String topicName = externalTask.getTopicName();

        List<Scenario> scenarios = queryScenario.queryScenarios(topicName);

        try {

            Scenario scenario = ScenarioHelper.extractScenario(externalTask, topicName, scenarios);

            DelayHelper.handleDelay(scenario.getDelay());

            ExceptionHelper.handlerFailure(scenario.getFailure());

            ExceptionHelper.handlerError(scenario.getError());

            Map<String, Object> handlerVariables = VariableHelper.handleVariables(externalTask, scenario);

            LOGGER.info("Execution completed with variables [[[{}]]]", handlerVariables);
            externalTaskService.complete(externalTask, handlerVariables);

        } catch (ServiceErrorException e) {
            LOGGER.warn("BPMN Error will be generated: " + e.getErrorCause(), e);
            externalTaskService.handleBpmnError(externalTask, e.getErrorCode(),
                    e.getErrorMessage());

        } catch (ServiceFailureException e) {
            LOGGER.warn("Failure will be generated: " + e.getErrorCause(), e);
            externalTaskService.handleFailure(externalTask, e.getMessage(), e.getDetails(),
                    Optional.of(e.getRetryTimes()).orElse(0),
                    Optional.of(e.getRetryTimeout()).orElse(0L));
        }

    }


}
