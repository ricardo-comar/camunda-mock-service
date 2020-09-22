package com.github.ricardocomar.springbootcamunda.mockservice.entrypoint;

import javax.transaction.Transactional;
import com.github.ricardocomar.springbootcamunda.mockservice.MockServiceApplication;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

@RunWith(SpringRunner.class)
@DirtiesContext
@SpringBootTest(classes = MockServiceApplication.class)
@Transactional
@ActiveProfiles("int-test")
public class ExternalServiceIntegrationTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8888, 8889);

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
                .perform(MockMvcRequestBuilders.get("/service/noTopic")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void testSaveScenario() throws Exception {

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/service/mockTopic")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/service/mockTopic")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/service/mockTopic")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/service/noTopic")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

}
