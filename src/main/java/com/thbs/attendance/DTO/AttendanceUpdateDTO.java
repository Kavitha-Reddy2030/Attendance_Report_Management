package com.thbs.attendance.DTO;

import java.util.List;

public class AttendanceUpdateDTO {
	
	
    private Long batchId;
    private Long courseId;
    private String date;
    private String type;
    private List<AttendanceDetailDTO> attendance;
	
    public Long getBatchId() {
		return batchId;
	}
    
	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}
	
	public Long getCourseId() {
		return courseId;
	}
	
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
	
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
	
	public List<AttendanceDetailDTO> getAttendance() {
		return attendance;
	}
	
	public void setAttendance(List<AttendanceDetailDTO> attendance) {
		this.attendance = attendance;
	}
	
	public AttendanceUpdateDTO(Long batchId, Long courseId, String date, String type,
			List<AttendanceDetailDTO> attendance) {
		super();
		this.batchId = batchId;
		this.courseId = courseId;
		this.date = date;
		this.type = type;
		this.attendance = attendance;
	}
	
	public AttendanceUpdateDTO() {
		super();
	}
    
	
}


