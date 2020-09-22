package com.github.ricardocomar.camunda.mockservice.entrypoint.handler;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import br.com.fluentvalidator.exception.ValidationException;

@ControllerAdvice
public class EntrypointExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ValidationException.class})
    @ResponseBody
    public ResponseEntity<Object> handleAccessDeniedException(Exception ex, WebRequest request) {

        ValidationException vex = (ValidationException) ex;
        return new ResponseEntity<Object>(vex.getValidationResult(), new HttpHeaders(),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public ResponseEntity<Object> handleControllerException(HttpServletRequest req, Throwable ex) {
        return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
