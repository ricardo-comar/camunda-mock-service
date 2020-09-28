package com.github.ricardocomar.camunda.mockservice.gateway.entity;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class FailureEmbeddableFixture implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(FailureEmbeddable.class).addTemplate("valid", new Rule() {
            {
                add("message", "FAILURE_FIXTURE");
                add("details", "Expected Failure");
                add("retryTimes", 3);
                add("retryTimeout", 1000L);
            }
        });
    }

}
