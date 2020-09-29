package com.github.ricardocomar.camunda.mockservice.handler.helper;

import com.github.ricardocomar.camunda.mockservice.handler.exception.ServiceErrorException;
import com.github.ricardocomar.camunda.mockservice.handler.exception.ServiceFailureException;
import com.github.ricardocomar.camunda.mockservice.handler.exception.ServiceHandlerException;
import com.github.ricardocomar.camunda.mockservice.model.ScenarioFailure;
import com.github.ricardocomar.camunda.mockservice.model.ScenarioError;
import org.junit.Test;

public class ExceptionHelperTest {

    @Test
    public void testEmptyFailure() throws ServiceHandlerException {
        ExceptionHelper.handlerFailure(null);
    }

    @Test(expected = ServiceFailureException.class)
    public void testFailure() throws ServiceHandlerException {
        ExceptionHelper.handlerFailure(new ScenarioFailure("message", "details", 1, 1L));
    }

    @Test
    public void testEmptyError() throws ServiceHandlerException {
        ExceptionHelper.handlerError(null);
    }

    @Test(expected = ServiceErrorException.class)
    public void testError() throws ServiceHandlerException {
        ExceptionHelper.handlerError(new ScenarioError("errorCode", "errorMessage"));
    }
}
