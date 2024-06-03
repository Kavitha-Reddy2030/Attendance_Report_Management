package com.thbs.attendance.exceptionhandlertest;

import com.thbs.attendance.exceptionhandler.AttendanceCalculationException;
import com.thbs.attendance.exceptionhandler.AttendanceNotFoundException;
import com.thbs.attendance.exceptionhandler.AttendanceReportException;
import com.thbs.attendance.exceptionhandler.BatchNotFoundException;
import com.thbs.attendance.exceptionhandler.CourseNotFoundException;
import com.thbs.attendance.exceptionhandler.EmployeeNotFoundException;
import com.thbs.attendance.exceptionhandler.ExternalServiceException;
import com.thbs.attendance.exceptionhandler.GlobalExceptionHandler;
import com.thbs.attendance.exceptionhandler.InvalidDateFormatException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GlobalExceptionHandlerTest {

	
    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    
    @Test
    public void testBatchNotFoundException() {
        BatchNotFoundException ex = new BatchNotFoundException("Batch not found");
        ResponseEntity<String> response = globalExceptionHandler.batchNotFoundException(ex);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Batch not found", response.getBody());
    }

    
    @Test
    public void testEmployeeNotFoundException() {
        EmployeeNotFoundException ex = new EmployeeNotFoundException("Employee not found");
        ResponseEntity<String> response = globalExceptionHandler.employeeNotFoundException(ex);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Employee not found", response.getBody());
    }

    
    @Test
    public void testInvalidDateFormatException() {
        InvalidDateFormatException ex = new InvalidDateFormatException("Invalid date format");
        ResponseEntity<String> response = globalExceptionHandler.invalidDateFormatException(ex);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Invalid date format", response.getBody());
    }
    
    
    @Test
    public void testAttendanceCalculationException() {
            AttendanceCalculationException ex = new AttendanceCalculationException("Attendance calculation error");
            ResponseEntity<String> response = globalExceptionHandler.attendanceCalculationException(ex);
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertEquals("Attendance calculation error", response.getBody());
    }

    
    @Test
    public void testAttendanceNotFoundException() {
            AttendanceNotFoundException ex = new AttendanceNotFoundException("Attendance not found");
            ResponseEntity<String> response = globalExceptionHandler.attendanceNotFoundException(ex);
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertEquals("Attendance not found", response.getBody());
    }

    
	@Test
	void testAttendanceReportException() {
		AttendanceReportException ex = new AttendanceReportException("Attendance report error");
		ResponseEntity<String> response = globalExceptionHandler.attendanceReportException(ex);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals("Attendance report error", response.getBody());
	}

	
	@Test
	void testCourseNotFoundException() {
		CourseNotFoundException ex = new CourseNotFoundException("Course not found");
		ResponseEntity<String> response = globalExceptionHandler.courseNotFoundException(ex);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals("Course not found", response.getBody());
	}

	
	@Test
	void testExternalServiceException() {
		ExternalServiceException ex = new ExternalServiceException("External service error");
		ResponseEntity<String> response = globalExceptionHandler.externalServiceException(ex);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals("External service error", response.getBody());
	}

   
}


