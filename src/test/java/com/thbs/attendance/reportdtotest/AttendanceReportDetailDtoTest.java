package com.thbs.attendance.reportdtotest;

import static org.junit.jupiter.api.Assertions.assertEquals; 
import org.junit.jupiter.api.Test;
import com.thbs.attendance.reportdto.AttendanceReportDetailDto;
 
public class AttendanceReportDetailDtoTest  {
 
	
    @Test
    void testDate() {
        // Create an instance of AttendanceReportDetailDto
        AttendanceReportDetailDto dto = new AttendanceReportDetailDto();
 
        // Test initial value
        assertEquals(null, dto.getDate());
 
        // Set a date and test
        String date = "2024-05-21";
        dto.setDate(date);
        assertEquals(date, dto.getDate());
    }
 
    
    @Test
    void testType() {
        // Create an instance of AttendanceReportDetailDto
        AttendanceReportDetailDto dto = new AttendanceReportDetailDto();
 
        // Test initial value
        assertEquals(null, dto.getType());
 
        // Set a type and test
        String type = "Full Day";
        dto.setType(type);
        assertEquals(type, dto.getType());
    }
 
    
    @Test
    void testStatus() {
        // Create an instance of AttendanceReportDetailDto
        AttendanceReportDetailDto dto = new AttendanceReportDetailDto();
 
        // Test initial value
        assertEquals(null, dto.getStatus());
 
        // Set a status and test
        String status = "Present";
        dto.setStatus(status);
        assertEquals(status, dto.getStatus());
    }
    
}
 