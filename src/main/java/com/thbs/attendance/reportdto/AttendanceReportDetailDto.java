package com.thbs.attendance.reportdto;


public class AttendanceReportDetailDto {

	private String date;
    private String type;
    private String status;
    
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public AttendanceReportDetailDto(String date, String type, String status) {
		super();
		this.date = date;
		this.type = type;
		this.status = status;
	}
	
	public AttendanceReportDetailDto() {
		super();
	}
    
	
}
