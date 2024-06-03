package com.thbs.attendance.Exception;

import org.springframework.http.HttpStatus;

public class Exception extends RuntimeException {
	
    private final String message;
    private final Throwable throwable;
    private final HttpStatus httpStatus;

    public Exception(String message) {
        super(message);
        this.message = message;
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        this.throwable = null;
    }

    public Exception(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.throwable = cause;
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public Exception(String message, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
        this.throwable = null;
    }

    public Exception(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.message = message;
        this.throwable = cause;
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}