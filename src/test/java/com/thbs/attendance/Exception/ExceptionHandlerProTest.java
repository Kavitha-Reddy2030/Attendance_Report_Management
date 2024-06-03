package com.thbs.attendance.Exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class ExceptionHandlerProTest {

    @Test
    public void testHandleUserNotFoundException() {
        // Test data
        UserNotFoundException ex = new UserNotFoundException("User not found");
        WebRequest request = mock(WebRequest.class);

        // Call the method
        ResponseEntity<Object> response = new ExceptionHandlerPro().handleUserNotFoundException(ex, request);

        // Verify response
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
    }

    @Test
    public void testHandleCourseNotFoundException() {
        // Test data
        CourseNotFoundException ex = new CourseNotFoundException("Course not found");
        WebRequest request = mock(WebRequest.class);

        // Call the method
        ResponseEntity<Object> response = new ExceptionHandlerPro().handleCourseNotFoundException(ex, request);

        // Verify response
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Course not found", response.getBody());
    }

    @Test
    public void testHandleBatchIdNotFoundException() {
        // Test data
        BatchIdNotFoundException ex = new BatchIdNotFoundException("Batch ID not found");
        WebRequest request = mock(WebRequest.class);

        // Call the method
        ResponseEntity<Object> response = new ExceptionHandlerPro().handleBatchIdNotFoundException(ex, request);

        // Verify response
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Batch ID not found", response.getBody());
    }

    @Test
    public void testHandleAttendenceRecordNotFound() {
        // Test data
        AttendenceRecordNotFound ex = new AttendenceRecordNotFound("Attendance record not found");
        WebRequest request = mock(WebRequest.class);

        // Call the method
        ResponseEntity<Object> response = new ExceptionHandlerPro().handleAttendenceRecordNotFound(ex, request);

        // Verify response
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("Attendance record not found", response.getBody());
    }
}
