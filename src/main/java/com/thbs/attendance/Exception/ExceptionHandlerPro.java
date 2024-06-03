package com.thbs.attendance.Exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ExceptionHandlerPro {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), ex.getHttpStatus());
    }

    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<Object> handleCourseNotFoundException(CourseNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), ex.getHttpStatus());
    }

    @ExceptionHandler(BatchIdNotFoundException.class)
    public ResponseEntity<Object> handleBatchIdNotFoundException(BatchIdNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), ex.getHttpStatus());
    }
   
    @ExceptionHandler(AttendenceRecordNotFound.class)
    public ResponseEntity<Object> handleAttendenceRecordNotFound(AttendenceRecordNotFound ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), ex.getHttpStatus());
    }
    
}