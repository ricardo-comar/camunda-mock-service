package com.github.ricardocomar.camunda.mockservice.handler.exception;

import lombok.Value;

@Value
public class ServiceErrorException extends ServiceHandlerException {

    private static final long serialVersionUID = 6334970435029469801L;

    private final String errorCause;

    private final String errorCode;
    
    private final String errorMessage;
    
}
