package com.github.ricardocomar.camunda.mockservice.handler.helper;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import com.github.ricardocomar.camunda.mockservice.model.Delay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DelayHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DelayHelper.class);

    private DelayHelper() {}

    public static final void handleDelay(Delay delay) {

        if (delay != null) {
            Integer wait = Optional.ofNullable(delay.getFixedMs()).orElseGet(
                    () -> ThreadLocalRandom.current().nextInt(delay.getMinMs(), delay.getMaxMs()));
            LOGGER.info("Execution will sleep for {}ms...", wait);

            try {
                TimeUnit.MILLISECONDS.sleep(wait);
            } catch (InterruptedException e) {
                LOGGER.info("Sleep finished ! Execution will carry on");
            }

        }
    }


}
