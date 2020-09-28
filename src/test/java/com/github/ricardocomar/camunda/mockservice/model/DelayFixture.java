package com.github.ricardocomar.camunda.mockservice.model;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class DelayFixture implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(Delay.class).addTemplate("valid-fixed", new Rule() {
            {
                add("fixedMs", 100);
            }
        });
        Fixture.of(Delay.class).addTemplate("valid-range", new Rule() {
            {
                add("minMs", 100);
                add("maxMs", 200);
            }
        });

    }

}
