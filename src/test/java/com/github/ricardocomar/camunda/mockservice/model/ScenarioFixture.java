package com.github.ricardocomar.camunda.mockservice.model;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class ScenarioFixture implements TemplateLoader {

    @Override
    public void load() {

        Fixture.of(Scenario.class).addTemplate("valid", new Rule() {
            {
                add("topicName", "mockTopic");
                add("priority", 1);
                add("delay", one(Delay.class, "valid-fixed"));
                add("condition", one(Condition.class, "valid"));
                add("variables", has(4).of(Variable.class, "boolean", "string", "long", "script"));
            }
        });

        Fixture.of(Scenario.class).addTemplate("valid-saved").inherits("valid", new Rule() {
            {
                add("scenarioId", "mockScenario");
            }
        });

        Fixture.of(Scenario.class).addTemplate("valid-1", new Rule() {
            {
                add("scenarioId", "mockScenario");
                add("topicName", "mockTopic");
                add("priority", 1);
                add("delay", one(Delay.class, "valid-fixed"));
                add("condition", one(Condition.class, "valid"));
                add("variables", has(1).of(Variable.class, "long"));
            }
        });

        Fixture.of(Scenario.class).addTemplate("valid-2", new Rule() {
            {
                add("scenarioId", "mockScenario");
                add("topicName", "mockTopic");
                add("priority", 2);
                add("delay", one(Delay.class, "valid-fixed"));
                add("condition", one(Condition.class, "valid"));
                add("variables", has(1).of(Variable.class, "script"));
            }
        });

        Fixture.of(Scenario.class).addTemplate("valid-falseCondition", new Rule() {
            {
                add("scenarioId", "mockScenario");
                add("topicName", "mockTopic");
                add("priority", 2);
                add("delay", one(Delay.class, "valid-fixed"));
                add("condition", one(Condition.class, "valid-false"));
                add("variables", has(1).of(Variable.class, "script"));
            }
        });

    }

}
