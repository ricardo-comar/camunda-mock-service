package com.github.ricardocomar.camunda.mockservice.handler.helper;

import java.util.Optional;
import com.github.ricardocomar.camunda.mockservice.handler.exception.ServiceErrorException;
import com.github.ricardocomar.camunda.mockservice.handler.exception.ServiceFailureException;
import com.github.ricardocomar.camunda.mockservice.model.ScenarioFailure;
import com.github.ricardocomar.camunda.mockservice.model.ScenarioError;

public class ExceptionHelper {

    private ExceptionHelper() {}

    public static final void handlerFailure(ScenarioFailure failure) throws ServiceFailureException {
        if (failure != null) {
            throw new ServiceFailureException("Scenario with expected failure",
                    failure.getMessage(), failure.getDetails(),
                    Optional.of(failure.getRetryTimes()).orElse(0),
                    Optional.of(failure.getRetryTimeout()).orElse(0L));
        }
    }

    public static final void handlerError(ScenarioError error) throws ServiceErrorException {
        if (error != null) {
            throw new ServiceErrorException("Scenario with expected failure",
                    error.getErrorCode(), error.getErrorMessage());
        }
    }

    
}
