package com.github.ricardocomar.camunda.mockservice.gateway.entity;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class ErrorEmbeddableFixture implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(ErrorEmbeddable.class).addTemplate("valid", new Rule() {
            {
                add("errorCode", "ERROR_FIXTURE");
                add("errorMessage", "Expected Error");
            }
        });
    }

}
