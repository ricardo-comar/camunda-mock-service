package com.github.ricardocomar.springbootcamunda.mockservice.model;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class VariableFixture implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(Variable.class).addTemplate("string", new Rule() {
            {
                add("name", "stringVariable");
                add("value", "String Value");
                add("className", "java.lang.String");
            }
        });
        Fixture.of(Variable.class).addTemplate("boolean", new Rule() {
            {
                add("name", "booleanVariable");
                add("value", "true");
                add("className", "java.lang.Boolean");
            }
        });
        Fixture.of(Variable.class).addTemplate("long", new Rule() {
            {
                add("name", "longVariable");
                add("value", "12345");
                add("className", "java.lang.Long");
            }
        });
        Fixture.of(Variable.class).addTemplate("script", new Rule() {
            {
                add("name", "scriptVariable");
                add("groovyScript", "return ['a', 'b', 'c']");
            }
        });
    }

}
