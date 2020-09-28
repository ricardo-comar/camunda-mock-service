package com.github.ricardocomar.camunda.mockservice.gateway.entity;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class DelayEmbeddedFixture implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(DelayEmbeddable.class).addTemplate("valid-fixed", new Rule() {
            {
                add("fixedMs", 100);
            }
        });
        Fixture.of(DelayEmbeddable.class).addTemplate("valid-range", new Rule() {
            {
                add("minMs", 100);
                add("maxMs", 200);
            }
        });

    }

}
