package com.thbs.attendance.reportdto;

import java.util.List;
import com.thbs.attendance.Entity.AttendanceDetail;

public class ReportExcelDto {

	private String id;
    private Long batchId;
    private Long courseId;
    private Long userId;
    private String email;
    private String name;
    private String BusinessUnit;
    private List<AttendanceDetail> attendance;
    
    public String getEmail() {
		return email;
	}
    
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getBusinessUnit() {
		return BusinessUnit;
	}
	
	public void setBusinessUnit(String businessUnit) {
		BusinessUnit = businessUnit;
	}
	
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
	
	public ReportExcelDto(String id, Long batchId, Long courseId, Long userId, String email, String name, String bU,
			List<AttendanceDetail> attendance) {
		super();
		this.id = id;
		this.batchId = batchId;
		this.courseId = courseId;
		this.userId = userId;
		this.email = email;
		this.name = name;
		BusinessUnit = bU;
		this.attendance = attendance;
	}
	
	public ReportExcelDto() {
		super();
	}
    
    
}
