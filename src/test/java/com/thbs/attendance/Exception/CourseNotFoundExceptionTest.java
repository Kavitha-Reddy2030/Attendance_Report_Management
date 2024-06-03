package com.thbs.attendance.Exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CourseNotFoundExceptionTest {

    @Test
    public void testCourseNotFoundExceptionConstructor() {
        // Test data
        String message = "Course not found";

        // Create CourseNotFoundException object using constructor
        CourseNotFoundException exception = new CourseNotFoundException(message);

        // Verify data
        assertEquals(message, exception.getMessage());
    }
    
}