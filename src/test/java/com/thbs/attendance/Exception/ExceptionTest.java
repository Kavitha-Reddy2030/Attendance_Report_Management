package com.thbs.attendance.Exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ExceptionTest {

    @Test
    public void testConstructorWithMessage() {
        // Test data
        String message = "Test message";

        // Create Exception object using constructor with message only
        Exception exception = new Exception(message);

        // Verify data
        assertEquals(message, exception.getMessage());
        assertNull(exception.getThrowable());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getHttpStatus());
    }

    @Test
    public void testConstructorWithMessageAndCause() {
        // Test data
        String message = "Test message";
        Throwable cause = new RuntimeException("Test cause");

        // Create Exception object using constructor with message and cause
        Exception exception = new Exception(message, cause);

        // Verify data
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getThrowable());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getHttpStatus());
    }

    @Test
    public void testConstructorWithMessageAndHttpStatus() {
        // Test data
        String message = "Test message";
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        // Create Exception object using constructor with message and HTTP status
        Exception exception = new Exception(message, httpStatus);

        // Verify data
        assertEquals(message, exception.getMessage());
        assertNull(exception.getThrowable());
        assertEquals(httpStatus, exception.getHttpStatus());
    }

    @Test
    public void testConstructorWithMessageCauseAndHttpStatus() {
        // Test data
        String message = "Test message";
        Throwable cause = new RuntimeException("Test cause");
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        // Create Exception object using constructor with message, cause, and HTTP status
        Exception exception = new Exception(message, cause, httpStatus);

        // Verify data
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getThrowable());
        assertEquals(httpStatus, exception.getHttpStatus());
    }
}
