package com.github.ricardocomar.camunda.mockservice.entrypoint;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ricardocomar.camunda.mockservice.MockServiceApplication;
import com.github.ricardocomar.camunda.mockservice.entrypoint.model.ScenarioRequest;
import com.github.ricardocomar.camunda.mockservice.model.Scenario;
import org.apache.commons.lang.builder.EqualsBuilder;
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
// @DirtiesContext
@SpringBootTest(classes = MockServiceApplication.class)
public class ScenarioIntTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

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
    public void testAbsentScenario() throws Exception {

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/scenario/noScenario")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testSaveScenario() throws Exception {

        ScenarioRequest request = Fixture.from(ScenarioRequest.class).gimme("valid");
        String requestString = OBJECT_MAPPER.writeValueAsString(request);

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
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.failure").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.variables").isNotEmpty()).andReturn()
                .getResponse().getContentAsString();

        Scenario created = OBJECT_MAPPER.readValue(responseString, Scenario.class);

        String contentGet = this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/scenario/{scenarioId}", created.getScenarioId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        Scenario queried = OBJECT_MAPPER.readValue(contentGet, Scenario.class);

        // Confirm both created and queried are equal
        assertThat(created, equalTo(queried));

        // Cannot insert with same topic and priority
        // this.mockMvc
        // .perform(MockMvcRequestBuilders.post("/scenario").content(requestString)
        // .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
        // .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testSaveScenarioError() throws Exception {

        ScenarioRequest request = Fixture.from(ScenarioRequest.class).gimme("valid-error");
        String requestString = OBJECT_MAPPER.writeValueAsString(request);

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
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.failure").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.variables").doesNotExist()).andReturn()
                .getResponse().getContentAsString();

        Scenario created = OBJECT_MAPPER.readValue(responseString, Scenario.class);

        String contentGet = this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/scenario/{scenarioId}", created.getScenarioId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        Scenario queried = OBJECT_MAPPER.readValue(contentGet, Scenario.class);

        // Confirm both created and queried are equal
        assertThat(created, equalTo(queried));

    }

    @Test
    public void testSaveScenarioFailure() throws Exception {

        ScenarioRequest request = Fixture.from(ScenarioRequest.class).gimme("valid-failure");
        String requestString = OBJECT_MAPPER.writeValueAsString(request);

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
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.failure").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.variables").doesNotExist()).andReturn()
                .getResponse().getContentAsString();

        Scenario created = OBJECT_MAPPER.readValue(responseString, Scenario.class);

        String contentGet = this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/scenario/{scenarioId}", created.getScenarioId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        Scenario queried = OBJECT_MAPPER.readValue(contentGet, Scenario.class);

        // Confirm both created and queried are equal
        assertThat(created, equalTo(queried));

    }

    @Test
    public void testUpdateScenario() throws Exception {

        ScenarioRequest request = Fixture.from(ScenarioRequest.class).gimme("valid");
        String requestString = OBJECT_MAPPER.writeValueAsString(request);

        Scenario created = OBJECT_MAPPER.readValue(this.mockMvc
                .perform(MockMvcRequestBuilders.post("/scenario").content(requestString)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString(), Scenario.class);

        Scenario updated = OBJECT_MAPPER.readValue(this.mockMvc
                .perform(MockMvcRequestBuilders.post("/scenario").content(requestString)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString(), Scenario.class);

        assertThat(created.getScenarioId(), not(equalTo(updated.getScenarioId())));
        assertThat(EqualsBuilder.reflectionEquals(created, updated, new String[] {"scenarioId"}),
                equalTo(true));

    }

    @Test
    public void testDeleteScenario() throws Exception {
        ScenarioRequest request = Fixture.from(ScenarioRequest.class).gimme("valid");
        String requestString = OBJECT_MAPPER.writeValueAsString(request);

        String responseString = this.mockMvc
                .perform(MockMvcRequestBuilders.post("/scenario").content(requestString)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn().getResponse()
                .getContentAsString();
        Scenario created = OBJECT_MAPPER.readValue(responseString, Scenario.class);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/scenario/{scenarioId}", created.getScenarioId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/scenario/{scenarioId}", created.getScenarioId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

        // Created scenario can't be returned again
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/scenario/{scenarioId}", created.getScenarioId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/scenario/{scenarioId}", created.getScenarioId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testExceptionHandler() throws Exception {

        ScenarioRequest request = Fixture.from(ScenarioRequest.class).gimme("valid");
        request.setCondition(null);
        String requestString = OBJECT_MAPPER.writeValueAsString(request);

        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/scenario").content(requestString)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

}
