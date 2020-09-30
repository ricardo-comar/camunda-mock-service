package com.github.ricardocomar.camunda.mockservice.handler.exception;

import lombok.Value;

@Value
public class ServiceFailureException extends ServiceHandlerException {

    private static final long serialVersionUID = 4176857709654641435L;

    private final String errorCause;

    private final String message;

    private final String details;

    private final Integer retryTimes;

    private final Long retryTimeout;

}
