package com.thbs.attendance.Exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends Exception {

    private final HttpStatus httpStatus;

    public NotFoundException(String message) {
        super(message);
        this.httpStatus = HttpStatus.NOT_FOUND;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
    
}