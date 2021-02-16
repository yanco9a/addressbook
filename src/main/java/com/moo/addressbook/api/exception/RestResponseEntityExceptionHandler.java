package com.moo.addressbook.api.exception;

import org.slf4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOG = getLogger(RestResponseEntityExceptionHandler.class.getCanonicalName());

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<Object> resourceNotFoundException(
            CustomerNotFoundException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                NOT_FOUND.value(),
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false));
        LOG.debug(format("exception: %s", ex.getLocalizedMessage()));
        LOG.debug(format("request: %s", request));
        return new ResponseEntity<>(message, getHttpHeaders(), NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> constraintViolationExceptionHandler(
            ConstraintViolationException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                BAD_REQUEST.value(),
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false));
        LOG.debug(format("exception: %s", ex.getLocalizedMessage()));
        LOG.debug(format("request: %s", request));
        return new ResponseEntity<>(message, getHttpHeaders(), BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> globalExceptionHandler(Exception ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false));
        LOG.debug(format("exception: %s", ex.getLocalizedMessage()));
        LOG.debug(format("request: %s", request));
        return new ResponseEntity<>(message, getHttpHeaders(), INTERNAL_SERVER_ERROR);
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(APPLICATION_JSON);
        return httpHeaders;
    }
}
