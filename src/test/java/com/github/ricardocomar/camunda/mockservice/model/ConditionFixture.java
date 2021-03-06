package com.github.ricardocomar.camunda.mockservice.model;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class ConditionFixture implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(Condition.class).addTemplate("valid", new Rule() {
            {
                add("conditionScript", "return true");
            }
        });
        Fixture.of(Condition.class).addTemplate("valid-false", new Rule() {
            {
                add("conditionScript", "return false");
            }
        });
    }

}
