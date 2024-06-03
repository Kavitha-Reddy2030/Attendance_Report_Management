package com.thbs.attendance.exceptionhandler;

public class InvalidDateFormatException extends RuntimeException  {

	public InvalidDateFormatException() {
		super();
	} 

	public InvalidDateFormatException(String message) {
		super(message);
	}

}




