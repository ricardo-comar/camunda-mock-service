package com.github.ricardocomar.camunda.mockservice.cron;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

public class MockCronService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MockCronService.class);

    @Scheduled(fixedDelay = 1000 * 60)    
    public void doSomething() {
        LOGGER.info("Just saying hello :)");
    }
}
