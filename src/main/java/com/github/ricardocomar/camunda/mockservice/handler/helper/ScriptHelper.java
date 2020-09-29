package com.github.ricardocomar.camunda.mockservice.handler.helper;

import java.util.Arrays;
import java.util.Map;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

public class ScriptHelper {
    
    @SafeVarargs
    public final Object evalScript(String script, Map<String, Object>... envVars)
            throws Exception {

        Binding binding = new Binding();
        Arrays.asList(envVars).stream()
                .forEach(e -> e.forEach((k, v) -> binding.setProperty(k, v)));

        return new GroovyShell(binding).evaluate(script);
    }
}
