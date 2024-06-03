package com.thbs.attendance.DTO;

public class AttendanceDetailDTO {

	
    private Long userId;
    private String status;
    
	public AttendanceDetailDTO(Long userId, String status) {
		super();
		this.userId = userId;
		this.status = status;
	}
	
	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public AttendanceDetailDTO() {
		super();
	}
	
	
}
