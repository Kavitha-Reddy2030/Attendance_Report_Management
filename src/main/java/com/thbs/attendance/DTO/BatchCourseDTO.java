package com.thbs.attendance.DTO;

import java.util.List;

public class BatchCourseDTO {
	
    private long batchId;
    private List<Courses> courses;
	
    public BatchCourseDTO(long batchId, List<Courses> courses) {
		super();
		this.batchId = batchId;
		this.courses = courses;
	}
    
	public long getBatchId() {
		return batchId;
	}
	
	public void setBatchId(long batchId) {
		this.batchId = batchId;
	}
	
	public List<Courses> getCourses() {
		return courses;
	}
	
	public void setCourses(List<Courses> courses) {
		this.courses = courses;
	}
	
	public BatchCourseDTO() {
		super();
	}
	
	
}
