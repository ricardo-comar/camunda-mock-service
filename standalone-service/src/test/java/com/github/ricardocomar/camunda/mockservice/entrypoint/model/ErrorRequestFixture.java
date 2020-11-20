package com.github.ricardocomar.camunda.mockservice.entrypoint.model;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class ErrorRequestFixture implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(ErrorRequest.class).addTemplate("valid", new Rule() {
            {
                add("errorCode", "ERROR_FIXTURE");
                add("errorMessage", "Expected Error");
            }
        });
    }

}
