package com.thbs.attendance.DTO;

public class EmployeeDTO {
	
    private long employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String businessUnit;
	
    public EmployeeDTO(long employeeId, String firstName, String lastName, String email, String role,
			String businessUnit) {
		super();
		this.employeeId = employeeId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.role = role;
		this.businessUnit = businessUnit;
	}
    
	public EmployeeDTO() {
		super();
	}
	
	public long getEmployeeId() {
		return employeeId;
	}
	
	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
	
	public String getBusinessUnit() {
		return businessUnit;
	}
	
	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}
	
    
}