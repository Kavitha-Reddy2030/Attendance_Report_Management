package com.thbs.attendance.Entity;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
public class Attendance {
	
    @Id
    private String id;
    private Long batchId;
    private Long courseId;
    private Long userId;
    private List<AttendanceDetail> attendance;

    public Attendance(Long batchid,Long courseId,Long userId,List<AttendanceDetail> attendanceDetails){
        this.batchId=batchid;
        this.courseId=courseId;
        this.userId=userId;
        this.attendance=attendanceDetails;
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

	public Attendance(String id, Long batchId, Long courseId, Long userId, List<AttendanceDetail> attendance) {
		super();
		this.id = id;
		this.batchId = batchId;
		this.courseId = courseId;
		this.userId = userId;
		this.attendance = attendance;
	}

	public Attendance() {
		super();
	}
    
    
}
