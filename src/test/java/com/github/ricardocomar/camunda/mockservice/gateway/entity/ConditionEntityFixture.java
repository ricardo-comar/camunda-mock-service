package com.github.ricardocomar.camunda.mockservice.gateway.entity;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class ConditionEntityFixture implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(ConditionEntity.class).addTemplate("valid", new Rule() {
            {
                add("conditionScript", "return true");
            }
        });
    }

}
