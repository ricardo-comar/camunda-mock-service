package com.github.ricardocomar.camunda.mockservice.handler.helper;

import com.github.ricardocomar.camunda.mockservice.model.Delay;
import org.junit.Test;

public class DelayHelperTest {

    @Test(timeout = 200)
    public void testEmptyDelay() {
        DelayHelper.handleDelay(null);
    }

    @Test(timeout = 200)
    public void testFixedDelay() {
        DelayHelper.handleDelay(new Delay(100, null, null));
    }

    @Test(timeout = 200)
    public void testRangeDelay() {
        DelayHelper.handleDelay(new Delay(null, 100, 120));
    }
    
    
}
