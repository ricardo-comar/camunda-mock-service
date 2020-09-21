package com.github.ricardocomar.springbootcamunda.mockservice.gateway.mapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import com.github.ricardocomar.springbootcamunda.mockservice.MockServiceApplication;
import com.github.ricardocomar.springbootcamunda.mockservice.gateway.entity.ScenarioEntity;
import com.github.ricardocomar.springbootcamunda.mockservice.model.Scenario;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;


public class EntityMapperTest {

    ScenarioEntityMapper mapper = new ScenarioEntityMapperImpl();
    private final Scenario model;
    private final ScenarioEntity entity;

    public EntityMapperTest() {
        FixtureFactoryLoader.loadTemplates(MockServiceApplication.class.getPackage().getName());
        ReflectionTestUtils.setField(mapper, "variableEntityMapper",
                new VariableEntityMapperImpl());
        ReflectionTestUtils.setField(mapper, "conditionEntityMapper",
                new ConditionEntityMapperImpl());

        model = Fixture.from(Scenario.class).gimme("valid");
        entity = Fixture.from(ScenarioEntity.class).gimme("valid");
    }

    @Test
    public void testModelToEntity() {
        assertThat(model, equalTo(mapper.fromEntity(entity)));
    }

    @Test
    public void testEntityToModel() {
        assertThat(model, equalTo(mapper.fromEntity(entity)));
    }

}
