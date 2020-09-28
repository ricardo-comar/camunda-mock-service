package com.github.ricardocomar.camunda.mockservice.gateway.entity;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class ScenarioEntityFixture implements TemplateLoader {

    @Override
    public void load() {

        Fixture.of(ScenarioEntity.class).addTemplate("valid", new Rule() {
            {
                add("topicName", "mockTopic");
                add("priority", 1);
                add("delay", one(DelayEmbeddable.class, "valid-fixed"));
                add("condition", one(ConditionEntity.class, "valid"));
                add("variables", has(4).of(VariableEntity.class, "boolean", "string", "long", "script"));
            }
        });
        Fixture.of(ScenarioEntity.class).addTemplate("valid-saved").inherits("valid", new Rule() {
            {
                add("scenarioId", "mockScenario");
            }
        });

    }

}
