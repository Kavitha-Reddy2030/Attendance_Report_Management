package com.thbs.attendance.DTO;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AttendanceUpdateDTOTest {

    @Test
    public void testAttendanceUpdateDTO() {
        // Test data
        Long batchId = 1L;
        Long courseId = 2L;
        String date = "2024-05-11";
        String type = "type";
        List<AttendanceDetailDTO> attendance = new ArrayList<>();

        // Create AttendanceUpdateDTO object
        AttendanceUpdateDTO attendanceUpdateDTO = new AttendanceUpdateDTO(batchId, courseId, date, type, attendance);

        // Verify data
        assertEquals(batchId, attendanceUpdateDTO.getBatchId());
        assertEquals(courseId, attendanceUpdateDTO.getCourseId());
        assertEquals(date, attendanceUpdateDTO.getDate());
        assertEquals(type, attendanceUpdateDTO.getType());
        assertEquals(attendance, attendanceUpdateDTO.getAttendance());
    }

    @Test
    public void testAttendanceUpdateDTOSetterGetter() {
        // Test data
        Long batchId = 1L;
        Long courseId = 2L;
        String date = "2024-05-11";
        String type = "type";
        List<AttendanceDetailDTO> attendance = new ArrayList<>();

        // Create AttendanceUpdateDTO object
        AttendanceUpdateDTO attendanceUpdateDTO = new AttendanceUpdateDTO();

        // Set data using setters
        attendanceUpdateDTO.setBatchId(batchId);
        attendanceUpdateDTO.setCourseId(courseId);
        attendanceUpdateDTO.setDate(date);
        attendanceUpdateDTO.setType(type);
        attendanceUpdateDTO.setAttendance(attendance);

        // Verify data using getters
        assertEquals(batchId, attendanceUpdateDTO.getBatchId());
        assertEquals(courseId, attendanceUpdateDTO.getCourseId());
        assertEquals(date, attendanceUpdateDTO.getDate());
        assertEquals(type, attendanceUpdateDTO.getType());
        assertEquals(attendance, attendanceUpdateDTO.getAttendance());
    }
}