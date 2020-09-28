package com.github.ricardocomar.camunda.mockservice.gateway.mapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToObject;
import com.github.ricardocomar.camunda.mockservice.MockServiceApplication;
import com.github.ricardocomar.camunda.mockservice.gateway.entity.ScenarioEntity;
import com.github.ricardocomar.camunda.mockservice.model.Scenario;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;


@RunWith(MockitoJUnitRunner.Silent.class)
public class EntityMapperTest {

    @InjectMocks
    ScenarioEntityMapper mapper = new ScenarioEntityMapperImpl();

    @Spy
    VariableEntityMapper variableEntityMapper = new VariableEntityMapperImpl();

    @Spy
    ConditionEntityMapper conditionEntityMapper = new ConditionEntityMapperImpl();

    @Spy
    DelayEmbeddableMapper delayEmbeddableMapper = new DelayEmbeddableMapperImpl();


    public EntityMapperTest() {
        FixtureFactoryLoader.loadTemplates(MockServiceApplication.class.getPackage().getName());
    }

    @Test
    public void testModelToEntity() {

        assertThat(Fixture.from(ScenarioEntity.class).gimme("valid-saved"), equalToObject(
                mapper.fromModel(Fixture.from(Scenario.class).gimme("valid-saved"))));

    }

    @Test
    public void testEntityToModel() {
        assertThat(Fixture.from(Scenario.class).gimme("valid-saved"), equalToObject(
                mapper.fromEntity(Fixture.from(ScenarioEntity.class).gimme("valid-saved"))));
    }

}
