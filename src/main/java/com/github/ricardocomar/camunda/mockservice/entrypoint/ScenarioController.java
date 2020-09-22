package com.github.ricardocomar.camunda.mockservice.entrypoint;

import java.util.Optional;
import com.github.ricardocomar.camunda.mockservice.entrypoint.mapper.ScenarioRequestMapper;
import com.github.ricardocomar.camunda.mockservice.entrypoint.model.ScenarioRequest;
import com.github.ricardocomar.camunda.mockservice.model.Scenario;
import com.github.ricardocomar.camunda.mockservice.usecase.QueryScenarioUseCase;
import com.github.ricardocomar.camunda.mockservice.usecase.RemoveScenarioUseCase;
import com.github.ricardocomar.camunda.mockservice.usecase.SaveScenarioUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class ScenarioController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScenarioController.class);

    @Autowired
    private QueryScenarioUseCase queryScenario;

    @Autowired
    private SaveScenarioUseCase saveScenario;

    @Autowired
    private RemoveScenarioUseCase removeScenario;

    @Autowired
    private ScenarioRequestMapper mapper;

    @PostMapping(path = "/scenario", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerScenario(
            @RequestBody(required = true) final ScenarioRequest request) {

        Optional<Scenario> scenario = queryScenario.queryDuplicated(request.getTopicName(), request.getPriority());
        if (scenario.isPresent()) {
            LOGGER.warn("Scenario for topic {} and priority {} found", request.getTopicName(), request.getPriority());
            return ResponseEntity.badRequest().build();
        }

        Scenario newScenario = saveScenario.save(mapper.fromRequest(request));

        LOGGER.info("Scenario {} for topic {} created", newScenario.getScenarioId(), newScenario.getTopicName());
        return ResponseEntity.status(HttpStatus.CREATED).body(newScenario);

    }

    @GetMapping(path = "/scenario/{topic}/{scenarioId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> queryScenario(@PathVariable final String topic,
            @PathVariable final String scenarioId) {

        Optional<Scenario> scenario = queryScenario.queryScenario(topic, scenarioId);
        if (scenario.isEmpty()) {
            LOGGER.warn("Scenario {} for topic {} not found", scenarioId, topic);
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().body(scenario.get());

    }

    @DeleteMapping(path = "/scenario/{topic}/{scenarioId}")
    public ResponseEntity<?> deleteScenario(@PathVariable final String topic,
            @PathVariable final String scenarioId) {

        Optional<Scenario> scenario = queryScenario.queryScenario(topic, scenarioId);
        if (scenario.isEmpty()) {
            LOGGER.warn("Scenario {} for topic {} not found", scenarioId, topic);
            return ResponseEntity.badRequest().build();
        }

        removeScenario.remove(scenario.get());

        return ResponseEntity.ok().body(scenario.get());
    }


}
