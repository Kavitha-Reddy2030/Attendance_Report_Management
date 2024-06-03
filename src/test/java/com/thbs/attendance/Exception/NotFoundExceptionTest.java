package com.thbs.attendance.Exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NotFoundExceptionTest {

    @Test
    public void testConstructor() {
        // Test data
        String message = "Resource not found";

        // Create NotFoundException object using constructor
        NotFoundException exception = new NotFoundException(message);

        // Verify data
        assertEquals(message, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }
    
}
