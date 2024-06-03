package com.thbs.attendance.DTO;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmployeeDTOTest {

    @Test
    public void testEmployeeDTOConstructor() {
        // Test data
        long employeeId = 1L;
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String role = "Developer";
        String businessUnit = "IT";

        // Create EmployeeDTO object using constructor
        EmployeeDTO employeeDTO = new EmployeeDTO(employeeId, firstName, lastName, email, role, businessUnit);

        // Verify data
        assertEquals(employeeId, employeeDTO.getEmployeeId());
        assertEquals(firstName, employeeDTO.getFirstName());
        assertEquals(lastName, employeeDTO.getLastName());
        assertEquals(email, employeeDTO.getEmail());
        assertEquals(role, employeeDTO.getRole());
        assertEquals(businessUnit, employeeDTO.getBusinessUnit());
    }

    @Test
    public void testEmployeeDTOSetterGetter() {
        // Test data
        long employeeId = 1L;
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String role = "Developer";
        String businessUnit = "IT";

        // Create EmployeeDTO object
        EmployeeDTO employeeDTO = new EmployeeDTO();

        // Set data using setters
        employeeDTO.setEmployeeId(employeeId);
        employeeDTO.setFirstName(firstName);
        employeeDTO.setLastName(lastName);
        employeeDTO.setEmail(email);
        employeeDTO.setRole(role);
        employeeDTO.setBusinessUnit(businessUnit);

        // Verify data using getters
        assertEquals(employeeId, employeeDTO.getEmployeeId());
        assertEquals(firstName, employeeDTO.getFirstName());
        assertEquals(lastName, employeeDTO.getLastName());
        assertEquals(email, employeeDTO.getEmail());
        assertEquals(role, employeeDTO.getRole());
        assertEquals(businessUnit, employeeDTO.getBusinessUnit());
    }
}