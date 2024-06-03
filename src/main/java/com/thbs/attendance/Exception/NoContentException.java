package com.thbs.attendance.Exception;

import org.springframework.http.HttpStatus;

public class NoContentException extends Exception {

    private final HttpStatus httpStatus;

    public NoContentException(String message) {
        super(message);
        this.httpStatus = HttpStatus.NO_CONTENT;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
    
}