package com.thbs.attendance.exceptionhandler;
 
public class ExternalServiceException extends RuntimeException {
	
	public ExternalServiceException() {
		super();
	}
	
	public ExternalServiceException(String message) {
		super(message);
	}
	
}