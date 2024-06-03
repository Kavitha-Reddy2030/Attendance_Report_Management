package com.thbs.attendance.DTO;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AttendanceDetailDTOTest {

    @Test
    public void testAttendanceDetailDTO() {
        // Test data
        Long userId = 123L;
        String status = "Present";

        // Create AttendanceDetailDTO object
        AttendanceDetailDTO attendanceDetailDTO = new AttendanceDetailDTO(userId, status);

        // Verify data
        assertEquals(userId, attendanceDetailDTO.getUserId());
        assertEquals(status, attendanceDetailDTO.getStatus());
    }

    @Test
    public void testAttendanceDetailDTOSetterGetter() {
        // Test data
        Long userId = 123L;
        String status = "Absent";

        // Create AttendanceDetailDTO object
        AttendanceDetailDTO attendanceDetailDTO = new AttendanceDetailDTO();

        // Set data using setters
        attendanceDetailDTO.setUserId(userId);
        attendanceDetailDTO.setStatus(status);

        // Verify data using getters
        assertEquals(userId, attendanceDetailDTO.getUserId());
        assertEquals(status, attendanceDetailDTO.getStatus());
    }
}