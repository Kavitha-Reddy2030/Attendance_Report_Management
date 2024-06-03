package com.thbs.attendance.exceptionhandler;

public class CourseNotFoundException extends RuntimeException {

	public CourseNotFoundException() {
		super();
	}

	public CourseNotFoundException(String message) {
		super(message);
	}

}

