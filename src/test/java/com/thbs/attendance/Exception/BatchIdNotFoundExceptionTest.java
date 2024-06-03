package com.thbs.attendance.Exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BatchIdNotFoundExceptionTest {

    @Test
    public void testBatchIdNotFoundExceptionConstructor() {
        // Test data
        String message = "Batch ID not found";

        // Create BatchIdNotFoundException object using constructor
        BatchIdNotFoundException exception = new BatchIdNotFoundException(message);

        // Verify data
        assertEquals(message, exception.getMessage());
    }
    
}
