package com.thbs.attendance.Entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;

public class AttendanceTest {

    @Test
    public void testAttendanceConstructor() {
        // Test data
        String id = "1";
        Long batchId = 1L;
        Long courseId = 2L;
        Long userId = 3L;
        List<AttendanceDetail> attendanceDetails = new ArrayList<>();

        // Create Attendance object using constructor
        Attendance attendance = new Attendance(id, batchId, courseId, userId, attendanceDetails);

        // Verify data
        assertEquals(id, attendance.getId());
        assertEquals(batchId, attendance.getBatchId());
        assertEquals(courseId, attendance.getCourseId());
        assertEquals(userId, attendance.getUserId());
        assertEquals(attendanceDetails, attendance.getAttendance());
    }

    
    @Test
    public void testAttendanceParameterizedConstructor() {
        // Test data
        Long batchId = 1L;
        Long courseId = 2L;
        Long userId = 3L;
        List<AttendanceDetail> attendanceDetails = new ArrayList<>();

        // Create Attendance object using parameterized constructor
        Attendance attendance = new Attendance(batchId, courseId, userId, attendanceDetails);

        // Verify data
        assertEquals(batchId, attendance.getBatchId());
        assertEquals(courseId, attendance.getCourseId());
        assertEquals(userId, attendance.getUserId());
        assertEquals(attendanceDetails, attendance.getAttendance());
    }
    
}
