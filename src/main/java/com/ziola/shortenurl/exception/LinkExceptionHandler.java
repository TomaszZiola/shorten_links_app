package com.ziola.shortenurl.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class LinkExceptionHandler {

    @ExceptionHandler({LinkException.class, LinkWrongFormatException.class})
    public ResponseEntity handleExceptions(RuntimeException ex) {
        String bodyOfResponse = ex.getMessage();
        return buildResponse(new Error(HttpStatus.BAD_REQUEST, bodyOfResponse, ex));
    }

    private ResponseEntity<Object> buildResponse(Error error) {
        return new ResponseEntity<>(error, error.getStatus());
    }
}
