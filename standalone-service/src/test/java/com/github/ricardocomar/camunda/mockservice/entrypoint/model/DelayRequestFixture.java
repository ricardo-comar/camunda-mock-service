package com.github.ricardocomar.camunda.mockservice.entrypoint.model;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class DelayRequestFixture implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(DelayRequest.class).addTemplate("valid-fixed", new Rule() {
            {
                add("fixedMs", 100);
            }
        });
        Fixture.of(DelayRequest.class).addTemplate("valid-range", new Rule() {
            {
                add("minMs", 100);
                add("maxMs", 200);
            }
        });

    }

}
