package com.github.ricardocomar.springbootcamunda.mockservice.model;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class ScenarioFixture implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(Scenario.class).addTemplate("valid", new Rule() {
            {
                add("scenarioId", "mockScenario");
                add("topicName", "mockTopic");
                add("order", 1L);
                add("condition", one(Condition.class, "valid"));
                add("variables", has(4).of(Variable.class, "boolean", "string", "long", "script"));
            }
        });

    }

}
