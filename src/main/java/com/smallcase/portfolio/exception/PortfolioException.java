package com.smallcase.portfolio.exception;

import org.springframework.http.HttpStatus;

/**
 * @author utsav
 * This class is used to define custom exception for our application
 * It helps return output - status code and message to user
 */
public class PortfolioException extends RuntimeException {

    private HttpStatus httpStatus;

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * @param message the detail message.
     */
    public PortfolioException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }
}