package com.thbs.attendance.Entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AttendanceDetailTest {
	
	
    @Test
    public void testAttendanceDetailConstructor() {
        // Test data
        String date = "2024-05-11";
        String type = "type";
        String status = "Present";

        // Create AttendanceDetail object using constructor
        AttendanceDetail attendanceDetail = new AttendanceDetail(date, type, status);

        // Verify data
        assertEquals(date, attendanceDetail.getDate());
        assertEquals(type, attendanceDetail.getType());
        assertEquals(status, attendanceDetail.getStatus());
    }

    
    @Test
    public void testAttendanceDetailSetterGetter() {
        // Test data
        String date = "2024-05-11";
        String type = "type";
        String status = "Present";

        // Create AttendanceDetail object
        AttendanceDetail attendanceDetail = new AttendanceDetail();

        // Set data using setters
        attendanceDetail.setDate(date);
        attendanceDetail.setType(type);
        attendanceDetail.setStatus(status);

        // Verify data using getters
        assertEquals(date, attendanceDetail.getDate());
        assertEquals(type, attendanceDetail.getType());
        assertEquals(status, attendanceDetail.getStatus());
    }
    
}
