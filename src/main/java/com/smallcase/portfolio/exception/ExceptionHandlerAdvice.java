package com.smallcase.portfolio.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author utsav
 * This class is used to handle PortfolioException thrown in the code and return it user as a response
 */
@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(PortfolioException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity handleException(PortfolioException e) {
        System.out.println(e.getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }
}

