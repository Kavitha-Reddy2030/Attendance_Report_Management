package com.thbs.attendance.reportdtotest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import com.thbs.attendance.Entity.AttendanceDetail;
import com.thbs.attendance.reportdto.ReportExcelDto;
 
public class ReportExcelDtoTest {
 
	
    @Test
    void testId() {
        // Create an instance of ReportExcelDto
        ReportExcelDto dto = new ReportExcelDto();
 
        // Test initial value
        assertNull(dto.getId());
 
        // Set an id and test
        String id = "123";
        dto.setId(id);
        assertEquals(id, dto.getId());
    }
 
    
    @Test
    void testBatchId() {
        // Create an instance of ReportExcelDto
        ReportExcelDto dto = new ReportExcelDto();
 
        // Test initial value
        assertNull(dto.getBatchId());
 
        // Set a batchId and test
        Long batchId = 1L;
        dto.setBatchId(batchId);
        assertEquals(batchId, dto.getBatchId());
    }
 
    
    @Test
    void testCourseId() {
        // Create an instance of ReportExcelDto
        ReportExcelDto dto = new ReportExcelDto();
 
        // Test initial value
        assertNull(dto.getCourseId());
 
        // Set a courseId and test
        Long courseId = 2L;
        dto.setCourseId(courseId);
        assertEquals(courseId, dto.getCourseId());
    }
 
    
    @Test
    void testUserId() {
        // Create an instance of ReportExcelDto
        ReportExcelDto dto = new ReportExcelDto();
 
        // Test initial value
        assertNull(dto.getUserId());
 
        // Set a userId and test
        Long userId = 3L;
        dto.setUserId(userId);
        assertEquals(userId, dto.getUserId());
    }
 
    
    @Test
    void testEmail() {
        // Create an instance of ReportExcelDto
        ReportExcelDto dto = new ReportExcelDto();
 
        // Test initial value
        assertNull(dto.getEmail());
 
        // Set an email and test
        String email = "test@example.com";
        dto.setEmail(email);
        assertEquals(email, dto.getEmail());
    }
 
    
    @Test
    void testName() {
        // Create an instance of ReportExcelDto
        ReportExcelDto dto = new ReportExcelDto();
 
        // Test initial value
        assertNull(dto.getName());
 
        // Set a name and test
        String name = "John Doe";
        dto.setName(name);
        assertEquals(name, dto.getName());
    }
 
    
    @Test
    void testBusinessUnit() {
        // Create an instance of ReportExcelDto
        ReportExcelDto dto = new ReportExcelDto();
 
        // Test initial value
        assertNull(dto.getBusinessUnit());
 
        // Set a business unit and test
        String businessUnit = "HR";
        dto.setBusinessUnit(businessUnit);
        assertEquals(businessUnit, dto.getBusinessUnit());
    }
 
    
    @Test
    void testAttendance() {
        // Create an instance of ReportExcelDto
        ReportExcelDto dto = new ReportExcelDto();
 
        // Test initial value
        assertNull(dto.getAttendance());
 
        // Set a list of AttendanceDetail and test
        List<AttendanceDetail> attendance = new ArrayList<>();
        attendance.add(new AttendanceDetail());
        dto.setAttendance(attendance);
        assertEquals(attendance, dto.getAttendance());
    }
    
    
}
 