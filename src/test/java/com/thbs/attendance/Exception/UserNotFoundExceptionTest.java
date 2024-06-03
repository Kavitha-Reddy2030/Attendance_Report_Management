package com.thbs.attendance.Exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserNotFoundExceptionTest {

    @Test
    public void testConstructor() {
        // Test data
        String message = "User not found";

        // Create UserNotFoundException object using constructor
        UserNotFoundException exception = new UserNotFoundException(message);

        // Verify data
        assertEquals(message, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }
    
}