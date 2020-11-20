package com.github.ricardocomar.camunda.mockservice.handler.exception;

public abstract class ServiceHandlerException extends Exception {

    private static final long serialVersionUID = 8381761965776033541L;
    
    public abstract String getErrorCause();
}
