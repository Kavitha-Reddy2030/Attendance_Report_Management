package com.thbs.attendance.reportdto;

import java.util.List;
import com.thbs.attendance.Entity.AttendanceDetail;

public class AttendanceReportDto {

	private String id;
    private Long batchId;
    private Long courseId;
    private Long userId;
    private List<AttendanceDetail> attendance;
    
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
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
	
	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public List<AttendanceDetail> getAttendance() {
		return attendance;
	}
	
	public void setAttendance(List<AttendanceDetail> attendance) {
		this.attendance = attendance;
	}
	
	public AttendanceReportDto(String id, Long batchId, Long courseId, Long userId, List<AttendanceDetail> attendance) {
		super();
		this.id = id;
		this.batchId = batchId;
		this.courseId = courseId;
		this.userId = userId;
		this.attendance = attendance;
	}
	
	public AttendanceReportDto() {
		super();
	}

    
}
