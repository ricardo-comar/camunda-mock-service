package com.github.ricardocomar.camunda.mockservice.runner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import com.github.ricardocomar.camunda.mockservice.MockServiceApplication;
import com.github.ricardocomar.camunda.mockservice.model.Scenario;
import com.github.ricardocomar.camunda.mockservice.usecase.QueryScenarioUseCase;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DirtiesContext
@SpringBootTest(classes = MockServiceApplication.class,
        properties = {"camunda.engine.url=http://localhost:8888/engine-rest"},
        args = "--scenariosFolder=src/test/resources/scenarios")
public class LoadScenariosRunnerTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8888);

    @Autowired
    public LoadScenariosRunner runner;

    @Autowired
    private QueryScenarioUseCase queryUC;

    @Test
    public void testLoadedScenarios() {

        final String topicName = "cardValidation";
        List<Scenario> scenarios = queryUC.queryScenarios(topicName);

        assertThat(scenarios.stream().filter(s -> topicName.equals(s.getTopicName()))
                .collect(Collectors.toList()), hasSize(3));
    }
}
