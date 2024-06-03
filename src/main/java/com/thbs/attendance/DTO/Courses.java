package com.thbs.attendance.DTO;

public class Courses {
	
    private long courseId;
    private String courseName;
	
    public Courses(long courseId, String courseName) {
		super();
		this.courseId = courseId;
		this.courseName = courseName;
	}
    
	public Courses() {
		super();
	}
	
	public long getCourseId() {
		return courseId;
	}
	
	public void setCourseId(long courseId) {
		this.courseId = courseId;
	}
	
	public String getCourseName() {
		return courseName;
	}
	
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	
	
}
