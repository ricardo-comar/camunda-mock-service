package com.github.ricardocomar.camunda.mockservice.entrypoint.model;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class ConditionRequestFixture implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(ConditionRequest.class).addTemplate("valid", new Rule() {
            {
                add("conditionScript", "return true");
            }
        });
    }

}
