package com.github.ricardocomar.springbootcamunda.mockservice.entrypoint;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import javax.transaction.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ricardocomar.springbootcamunda.mockservice.MockServiceApplication;
import com.github.ricardocomar.springbootcamunda.mockservice.entrypoint.model.ScenarioRequest;
import com.github.ricardocomar.springbootcamunda.mockservice.model.Scenario;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

@RunWith(SpringRunner.class)
@DirtiesContext
@SpringBootTest(classes = MockServiceApplication.class)
@Transactional
public class ScenarioIntegrationTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeClass
    public static void beforeClass() {
        FixtureFactoryLoader.loadTemplates(MockServiceApplication.class.getPackage().getName());
    }

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testInvalidGetScenario() throws Exception {

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/scenario/noTopic/noScenario")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testSaveScenario() throws Exception {

        ScenarioRequest request = Fixture.from(ScenarioRequest.class).gimme("valid");
        String requestString = new ObjectMapper().writeValueAsString(request);

        String responseString = this.mockMvc
                .perform(MockMvcRequestBuilders.post("/scenario").content(requestString)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.scenarioId").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.topicName")
                        .value(Matchers.is(request.getTopicName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.priority")
                        .value(Matchers.is(request.getPriority().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.condition.conditionScript")
                        .value(Matchers.is("return true")))
                .andReturn().getResponse().getContentAsString();

        ;

        Scenario created = new ObjectMapper().readValue(responseString, Scenario.class);

        String contentGet = this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/scenario/{topicName}/{scenarioId}", request.getTopicName(), created.getScenarioId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        Scenario queried = new ObjectMapper().readValue(contentGet, Scenario.class);

        assertThat(created, equalTo(queried));

    }

}
