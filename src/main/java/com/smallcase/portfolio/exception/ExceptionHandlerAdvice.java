package com.smallcase.portfolio.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author utsav
 * This class is used to handle PortfolioException thrown in the code and return it user as a response
 */
@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(PortfolioException.class)
    public ResponseEntity handleException(PortfolioException e) {
        System.out.println(e.getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }
}

