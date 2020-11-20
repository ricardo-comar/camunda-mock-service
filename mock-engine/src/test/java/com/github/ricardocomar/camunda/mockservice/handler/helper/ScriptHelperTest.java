package com.github.ricardocomar.camunda.mockservice.handler.helper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;

import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class ScriptHelperTest {

    private Map<String, Object> variables;
    private Map<String, Object> errors;


    @Before
    public void before() {
        variables = new HashMap<>();
        errors = new HashMap<>();
    }

    @Test
    public void testScriptValid() throws Exception {

        variables.put("myVar", 4);
        Object result = ScriptHelper.evalScript("return 7 * myVar", variables);

        assertThat(errors.values(), hasSize(0));
        assertThat(variables.values(), hasSize(1));
        assertThat(result, equalTo(28));
    }

    @Test(expected = Exception.class)
    @Ignore("Turn on when identify how to throw exception on runtime errors")
    public void testScriptError() throws Exception {

        variables.put("myVarX", 4);
        Object result = ScriptHelper.evalScript("return 7 * myVar", variables);

        assertThat(errors.values(), hasSize(1));
        assertThat(variables.values(), hasSize(1));
        assertThat(result, nullValue());
    }
}
