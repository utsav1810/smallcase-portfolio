package com.smallcase.portfolio.exception;

import com.smallcase.portfolio.exception.PortfolioException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(PortfolioException.class)
    public ResponseEntity handleException(PortfolioException e) {
        System.out.println(e.getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }
}

