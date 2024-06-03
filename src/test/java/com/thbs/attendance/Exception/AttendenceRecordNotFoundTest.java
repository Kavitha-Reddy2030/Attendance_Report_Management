package com.thbs.attendance.Exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AttendenceRecordNotFoundTest {

    @Test
    public void testAttendenceRecordNotFoundConstructor() {
        // Test data
        String message = "Attendance record not found";

        // Create AttendenceRecordNotFound object using constructor
        AttendenceRecordNotFound exception = new AttendenceRecordNotFound(message);

        // Verify data
        assertEquals(message, exception.getMessage());
    }
    
}
