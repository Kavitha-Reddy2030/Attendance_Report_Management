package com.thbs.attendance.exceptionhandler;
 
public class AttendanceNotFoundException extends RuntimeException {
	
	public AttendanceNotFoundException() {
		super();
	}
	
	public AttendanceNotFoundException(String message) {
		super(message);
	}
	
}