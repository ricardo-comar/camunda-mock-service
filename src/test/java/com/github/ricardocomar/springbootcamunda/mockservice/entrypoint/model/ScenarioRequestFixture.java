package com.github.ricardocomar.springbootcamunda.mockservice.entrypoint.model;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class ScenarioRequestFixture implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(ScenarioRequest.class).addTemplate("valid", new Rule() {
            {
                add("topicName", "mockTopic");
                add("condition", one(ConditionRequest.class, "valid"));
                add("priority", 1L);
                add("variables",
                        has(4).of(VariableRequest.class, "boolean", "string", "long", "script"));
            }
        });

    }

}
