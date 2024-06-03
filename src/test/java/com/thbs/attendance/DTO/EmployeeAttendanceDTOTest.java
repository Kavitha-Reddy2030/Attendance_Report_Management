package com.thbs.attendance.DTO;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmployeeAttendanceDTOTest {

    @Test
    public void testEmployeeAttendanceDTOConstructor() {
        // Test data
        long employeeId = 1L;
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String role = "Developer";
        String businessUnit = "IT";
        String status = "Present";

        // Create EmployeeAttendanceDTO object using constructor
        EmployeeAttendanceDTO employeeAttendanceDTO = new EmployeeAttendanceDTO(employeeId, firstName, lastName, email, role, businessUnit, status);

        // Verify data
        assertEquals(employeeId, employeeAttendanceDTO.getEmployeeId());
        assertEquals(firstName, employeeAttendanceDTO.getFirstName());
        assertEquals(lastName, employeeAttendanceDTO.getLastName());
        assertEquals(email, employeeAttendanceDTO.getEmail());
        assertEquals(role, employeeAttendanceDTO.getRole());
        assertEquals(businessUnit, employeeAttendanceDTO.getBusinessUnit());
        assertEquals(status, employeeAttendanceDTO.getStatus());
    }

    @Test
    public void testEmployeeAttendanceDTOSetterGetter() {
        // Test data
        long employeeId = 1L;
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String role = "Developer";
        String businessUnit = "IT";
        String status = "Present";

        // Create EmployeeAttendanceDTO object
        EmployeeAttendanceDTO employeeAttendanceDTO = new EmployeeAttendanceDTO();

        // Set data using setters
        employeeAttendanceDTO.setEmployeeId(employeeId);
        employeeAttendanceDTO.setFirstName(firstName);
        employeeAttendanceDTO.setLastName(lastName);
        employeeAttendanceDTO.setEmail(email);
        employeeAttendanceDTO.setRole(role);
        employeeAttendanceDTO.setBusinessUnit(businessUnit);
        employeeAttendanceDTO.setStatus(status);

        // Verify data using getters
        assertEquals(employeeId, employeeAttendanceDTO.getEmployeeId());
        assertEquals(firstName, employeeAttendanceDTO.getFirstName());
        assertEquals(lastName, employeeAttendanceDTO.getLastName());
        assertEquals(email, employeeAttendanceDTO.getEmail());
        assertEquals(role, employeeAttendanceDTO.getRole());
        assertEquals(businessUnit, employeeAttendanceDTO.getBusinessUnit());
        assertEquals(status, employeeAttendanceDTO.getStatus());
    }
}
