package com.thbs.attendance.exceptionhandler;
 
public class AttendanceCalculationException extends RuntimeException {
	
	public AttendanceCalculationException() {
		super();
	}
	
	public AttendanceCalculationException(String message) {
		super(message);
	}
	
}