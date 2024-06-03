package com.thbs.attendance.reportservicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import static org.mockito.ArgumentMatchers.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.thbs.attendance.Entity.Attendance;
import com.thbs.attendance.Entity.AttendanceDetail;
import com.thbs.attendance.Repository.AttendanceRepository;
import com.thbs.attendance.exceptionhandler.AttendanceCalculationException;
import com.thbs.attendance.exceptionhandler.BatchNotFoundException;
import com.thbs.attendance.exceptionhandler.CourseNotFoundException;
import com.thbs.attendance.exceptionhandler.EmployeeNotFoundException;
import com.thbs.attendance.exceptionhandler.ExternalServiceException;
import com.thbs.attendance.reportdto.ReportExcelDto;
import com.thbs.attendance.reportservice.AttendanceReportService;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class) 
public class AttendanceReportServiceTest {

	
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private AttendanceRepository attendanceRepository;

    @InjectMocks
    private AttendanceReportService attendanceReportService;
    
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    
    @Test
    public void testGenerateAttendanceReportDetails() {
        // Mock data
        Long batchId = 1L;
        Long courseId = 1L;
        String startDate = "2024-01-01";
        String endDate = "2024-01-31";

        List<Map<String, Object>> employeeDetails = new ArrayList<>();
        Map<String, Object> employee1 = new HashMap<>();
        employee1.put("employeeId", 1);
        employee1.put("email", "employee1@example.com");
        employee1.put("firstName", "John");
        employee1.put("lastName", "Doe");
        employee1.put("businessUnit", "BU1");
        employeeDetails.add(employee1);

        List<Attendance> attendances = new ArrayList<>();
        Attendance attendance = new Attendance();
        attendance.setId("1");
        attendance.setBatchId(1L);
        attendance.setCourseId(1L);
        attendance.setUserId(1L);
        attendance.setAttendance(new ArrayList<>());
        attendances.add(attendance);

        // Mock restTemplate exchange
        ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<>(employeeDetails, HttpStatus.OK);
        when(restTemplate.exchange(eq("http://BATCH-MANAGEMENT-SERVICE/batch/batch-details/employees/{batchId}"),
                eq(HttpMethod.GET), any(), any(ParameterizedTypeReference.class), eq(batchId)))
                .thenReturn(responseEntity);

        // Mock attendanceRepository findByBatchIdAndCourseIdAndUserIdInAndAttendance_DateBetween
        when(attendanceRepository.findByBatchIdAndCourseIdAndUserIdInAndAttendance_DateBetween(
                eq(batchId), eq(courseId), any(List.class), eq(startDate), eq(endDate)))
                .thenReturn(attendances);

        // Test the method
        List<ReportExcelDto> result = attendanceReportService.generateAttendanceReportDetails(batchId, courseId, startDate, endDate);

        // Assertions
        assertEquals(1, result.size());
        ReportExcelDto reportDto = result.get(0);
        assertEquals("1", reportDto.getId());
        assertEquals(1L, reportDto.getBatchId());
        assertEquals(1L, reportDto.getCourseId());
        assertEquals(1L, reportDto.getUserId());
        assertEquals("employee1@example.com", reportDto.getEmail());
        assertEquals("John Doe", reportDto.getName());
        assertEquals("BU1", reportDto.getBusinessUnit());
        assertEquals(31, reportDto.getAttendance().size()); // Assuming there are 31 days in January
    }

    
    @Test
    public void testGenerateAttendanceReportDetailsNoEmployeeDetailsFound() {
        Long batchId = 1L;
        Long courseId = 1L;
        String startDate = "2024-01-01";
        String endDate = "2024-01-31";

        when(restTemplate.exchange(eq("http://BATCH-MANAGEMENT-SERVICE/batch/batch-details/employees/{batchId}"),
                eq(HttpMethod.GET), any(), any(ParameterizedTypeReference.class), eq(batchId)))
                .thenReturn(ResponseEntity.ok(new ArrayList<>()));

        assertThrows(BatchNotFoundException.class, () -> {
            attendanceReportService.generateAttendanceReportDetails(batchId, courseId, startDate, endDate);
        });
    }

    
    @Test
    public void testGenerateAttendanceReportDetailsExternalServiceException() {
        Long batchId = 1L;
        Long courseId = 1L;
        String startDate = "2024-01-01";
        String endDate = "2024-01-31";

        when(restTemplate.exchange(eq("http://BATCH-MANAGEMENT-SERVICE/batch/batch-details/employees/{batchId}"),
                eq(HttpMethod.GET), any(), any(ParameterizedTypeReference.class), eq(batchId)))
                .thenThrow(new RuntimeException());

        assertThrows(ExternalServiceException.class, () -> {
            attendanceReportService.generateAttendanceReportDetails(batchId, courseId, startDate, endDate);
        });
    }
 
    
    @Test
    public void testGenerateAttendanceReportDetailsAttendanceRepositoryError() {
        Long batchId = 1L;
        Long courseId = 1L;
        String startDate = "2024-01-01";
        String endDate = "2024-01-31";

        List<Map<String, Object>> employeeDetails = new ArrayList<>();
        Map<String, Object> employeeDetail = new HashMap<>();
        employeeDetail.put("employeeId", 1);
        employeeDetails.add(employeeDetail);

        when(restTemplate.exchange(eq("http://BATCH-MANAGEMENT-SERVICE/batch/batch-details/employees/{batchId}"),
                eq(HttpMethod.GET), any(), any(ParameterizedTypeReference.class), eq(batchId)))
                .thenReturn(ResponseEntity.ok(employeeDetails));

        when(attendanceRepository.findByBatchIdAndCourseIdAndUserIdInAndAttendance_DateBetween(eq(batchId), eq(courseId),
                any(), eq(startDate), eq(endDate)))
                .thenThrow(new RuntimeException());

        assertThrows(AttendanceCalculationException.class, () -> {
            attendanceReportService.generateAttendanceReportDetails(batchId, courseId, startDate, endDate);
        });
    }

    
    @Test
    public void testGenerateAttendanceReportDetailsNoAttendanceRecordsFound() {
        Long batchId = 1L;
        Long courseId = 1L;
        String startDate = "2024-01-01";
        String endDate = "2024-01-31";

        List<Map<String, Object>> employeeDetails = new ArrayList<>();
        Map<String, Object> employeeDetail = new HashMap<>();
        employeeDetail.put("employeeId", 1);
        employeeDetails.add(employeeDetail);

        when(restTemplate.exchange(eq("http://BATCH-MANAGEMENT-SERVICE/batch/batch-details/employees/{batchId}"),
                eq(HttpMethod.GET), any(), any(ParameterizedTypeReference.class), eq(batchId)))
                .thenReturn(ResponseEntity.ok(employeeDetails));

        when(attendanceRepository.findByBatchIdAndCourseIdAndUserIdInAndAttendance_DateBetween(eq(batchId), eq(courseId),
                any(), eq(startDate), eq(endDate)))
                .thenReturn(new ArrayList<>());

        assertThrows(CourseNotFoundException.class, () -> {
            attendanceReportService.generateAttendanceReportDetails(batchId, courseId, startDate, endDate);
        });
    }
    
    
    @Test
    public void testGenerateAttendanceReportDetailsDate() {
        // Mock data
        Long batchId = 1L;
        Long courseId = 1L;
        String date = "2024-01-01";

        List<Map<String, Object>> employeeDetails = new ArrayList<>();
        Map<String, Object> employee1 = new HashMap<>();
        employee1.put("employeeId", 1);
        employee1.put("email", "employee1@example.com");
        employee1.put("firstName", "John");
        employee1.put("lastName", "Doe");
        employee1.put("businessUnit", "BU1");
        employeeDetails.add(employee1);

        List<Attendance> attendances = new ArrayList<>();
        Attendance attendance = new Attendance();
        attendance.setId("1");
        attendance.setBatchId(1L);
        attendance.setCourseId(1L);
        attendance.setUserId(1L);
        List<AttendanceDetail> attendanceDetails = new ArrayList<>();
        AttendanceDetail detail = new AttendanceDetail();
        detail.setDate(date);
        detail.setType("Type");
        detail.setStatus("Status");
        attendanceDetails.add(detail);
        attendance.setAttendance(attendanceDetails);
        attendances.add(attendance);

        // Mock restTemplate exchange
        ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<>(employeeDetails, HttpStatus.OK);
        when(restTemplate.exchange(eq("http://BATCH-MANAGEMENT-SERVICE/batch/batch-details/employees/{batchId}"),
                eq(HttpMethod.GET), any(), any(ParameterizedTypeReference.class), eq(batchId)))
                .thenReturn(responseEntity);

        // Mock attendanceRepository findByBatchIdAndCourseIdAndUserIdInAndAttendance_Date
        when(attendanceRepository.findByBatchIdAndCourseIdAndUserIdInAndAttendance_Date(
                eq(batchId), eq(courseId), any(List.class), eq(date)))
                .thenReturn(attendances);

        // Test the method
        List<ReportExcelDto> result = attendanceReportService.generateAttendanceReportDetails(batchId, courseId, date);

        // Assertions
        assertEquals(1, result.size());
        ReportExcelDto reportDto = result.get(0);
        assertEquals("1", reportDto.getId());
        assertEquals(1L, reportDto.getBatchId());
        assertEquals(1L, reportDto.getCourseId());
        assertEquals(1L, reportDto.getUserId());
        assertEquals("employee1@example.com", reportDto.getEmail());
        assertEquals("John Doe", reportDto.getName());
        assertEquals("BU1", reportDto.getBusinessUnit());
        assertEquals(1, reportDto.getAttendance().size());
        AttendanceDetail reportDetail = reportDto.getAttendance().get(0);
        assertEquals(date, reportDetail.getDate());
        assertEquals("Type", reportDetail.getType());
        assertEquals("Status", reportDetail.getStatus());
    }

    
    @Test
    public void testGenerateAttendanceExcelFile() throws IOException {
        // Mock data
        Long batchId = 1L;
        Long courseId = 1L;
        String startDate = "2024-01-01";
        String endDate = "2024-01-31";

        // Mock responseEntity for employee details
        List<Map<String, Object>> employeeDetails = new ArrayList<>();
        Map<String, Object> employee = new HashMap<>();
        employee.put("employeeId", 1);
        employee.put("email", "employee@example.com");
        employee.put("firstName", "John");
        employee.put("lastName", "Doe");
        employee.put("businessUnit", "BU1");
        employeeDetails.add(employee);
        ResponseEntity<List<Map<String, Object>>> responseEntity = ResponseEntity.ok(employeeDetails);
        when(restTemplate.exchange(eq("http://BATCH-MANAGEMENT-SERVICE/batch/batch-details/employees/{batchId}"),
                eq(HttpMethod.GET), any(), any(ParameterizedTypeReference.class), eq(batchId)))
                .thenReturn(responseEntity);

        // Mock attendance records
        List<Attendance> attendances = new ArrayList<>();
        Attendance attendance = new Attendance();
        attendance.setId("1");
        attendance.setBatchId(batchId);
        attendance.setCourseId(courseId);
        attendance.setUserId(1L);
        attendance.setAttendance(Arrays.asList(
                new AttendanceDetail("2024-01-01", "First Half", "Present"),
                new AttendanceDetail("2024-01-01", "Second Half", "Absent"),
                new AttendanceDetail("2024-01-02", "Full Day", "Present")
        ));
        attendances.add(attendance);
        when(attendanceRepository.findByBatchIdAndCourseIdAndUserIdInAndAttendance_DateBetween(eq(batchId), eq(courseId),
                any(List.class), eq(startDate), eq(endDate))).thenReturn(attendances);

        // Test generateAttendanceExcelFile method
        String fileName = "AttendanceReport.xlsx";
        attendanceReportService.generateAttendanceExcelFile(batchId, courseId, startDate, endDate);

        // Check if the file was created
        File file = new File(fileName);
        assertEquals(true, file.exists());

        // Check if the file content is correct (you can add more detailed checks if needed)
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            // Add assertions to check the content of the Excel file
            assertEquals("Attendance Report", sheet.getSheetName());
            // Add more assertions for the content if needed
            workbook.close();
        }

        // Clean up - delete the file
        file.delete();
    }
    
    
    @Test
    public void testGenerateAttendanceExcelFileDate() throws IOException {
        // Mock data
        Long batchId = 1L;
        Long courseId = 1L;
        String date = "2024-01-01";

        // Mock responseEntity for employee details
        List<Map<String, Object>> employeeDetails = new ArrayList<>();
        Map<String, Object> employee = new HashMap<>();
        employee.put("employeeId", 1);
        employee.put("email", "employee@example.com");
        employee.put("firstName", "John");
        employee.put("lastName", "Doe");
        employee.put("businessUnit", "BU1");
        employeeDetails.add(employee);
        ResponseEntity<List<Map<String, Object>>> responseEntity = ResponseEntity.ok(employeeDetails);
        when(restTemplate.exchange(eq("http://BATCH-MANAGEMENT-SERVICE/batch/batch-details/employees/{batchId}"),
                eq(HttpMethod.GET), any(), any(ParameterizedTypeReference.class), eq(batchId)))
                .thenReturn(responseEntity);

        // Mock attendance records
        List<Attendance> attendances = new ArrayList<>();
        Attendance attendance = new Attendance();
        attendance.setId("1");
        attendance.setBatchId(batchId);
        attendance.setCourseId(courseId);
        attendance.setUserId(1L);
        attendance.setAttendance(Arrays.asList(
                new AttendanceDetail("2024-01-01", "First Half", "Present"),
                new AttendanceDetail("2024-01-01", "Second Half", "Absent")
        ));
        attendances.add(attendance);
        when(attendanceRepository.findByBatchIdAndCourseIdAndUserIdInAndAttendance_Date(eq(batchId), eq(courseId),
                any(List.class), eq(date))).thenReturn(attendances);

        // Test generateAttendanceExcelFile method
        String fileName = "AttendanceReport.xlsx";
        attendanceReportService.generateAttendanceExcelFile(batchId, courseId, date);

        // Check if the file was created
        File file = new File(fileName);
        assertEquals(true, file.exists());

        // Check if the file content is correct (you can add more detailed checks if needed)
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            // Add assertions to check the content of the Excel file
            assertEquals("Attendance Report", sheet.getSheetName());

            // Check headers
            assertEquals("Sl No", sheet.getRow(0).getCell(0).getStringCellValue());
            assertEquals("User Id", sheet.getRow(0).getCell(1).getStringCellValue());
            assertEquals("Name", sheet.getRow(0).getCell(2).getStringCellValue());
            assertEquals("Email Id", sheet.getRow(0).getCell(3).getStringCellValue());
            assertEquals("Business Unit", sheet.getRow(0).getCell(4).getStringCellValue());
            assertEquals("2024-01-01", sheet.getRow(0).getCell(5).getStringCellValue());
            assertEquals("First Half", sheet.getRow(1).getCell(5).getStringCellValue());
            assertEquals("Second Half", sheet.getRow(1).getCell(6).getStringCellValue());

            // Check data
            assertEquals(1, (int) sheet.getRow(2).getCell(0).getNumericCellValue());
            assertEquals(1L, (long) sheet.getRow(2).getCell(1).getNumericCellValue());
            assertEquals("John Doe", sheet.getRow(2).getCell(2).getStringCellValue());
            assertEquals("employee@example.com", sheet.getRow(2).getCell(3).getStringCellValue());
            assertEquals("BU1", sheet.getRow(2).getCell(4).getStringCellValue());
            assertEquals("Present", sheet.getRow(2).getCell(5).getStringCellValue());
            assertEquals("Absent", sheet.getRow(2).getCell(6).getStringCellValue());

            workbook.close();
        }

        // Clean up - delete the file
        file.delete();
    }
    
    
    @Test
    public void testGenerateEmployeeAttendanceReportSuccess() {
        // Arrange
        Long batchId = 1L;
        Long courseId = 1L;
        Long employeeId = 1L;
        String startDate = "2024-01-01";
        String endDate = "2024-01-31";

        // Mocking employee details response
        List<Map<String, Object>> employeeDetails = new ArrayList<>();
        Map<String, Object> employeeDetail = new HashMap<>();
        employeeDetail.put("employeeId", 1); // Assuming this is the ID of the employee
        // Add other employee details to the map
        employeeDetails.add(employeeDetail);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), any(ParameterizedTypeReference.class), eq(batchId)))
                .thenReturn(new ResponseEntity<>(employeeDetails, HttpStatus.OK));

        // Mocking attendance repository response
        List<Attendance> attendances = new ArrayList<>();
        // Add attendance records to the list

        when(attendanceRepository.findByBatchIdAndCourseIdAndUserIdAndAttendance_DateBetween(eq(batchId), eq(courseId),
                eq(employeeId), eq(startDate), eq(endDate)))
                .thenReturn(attendances);

        // Act
        List<ReportExcelDto> result = attendanceReportService.generateEmployeeAttendanceReport(batchId, courseId, employeeId, startDate, endDate);

        // Assert
        assertNotNull(result);
        // Add assertions for the generated report DTOs
    }

    
    @Test
    public void testGenerateEmployeeAttendanceReport_EmployeeNotFound() {
        // Mock data
        Long batchId = 1L;
        Long courseId = 1L;
        Long employeeId = 2L; // Non-existent employee
        String startDate = "2024-01-01";
        String endDate = "2024-01-10";

        // Mock responseEntity for employee details
        List<Map<String, Object>> employeeDetails = new ArrayList<>();
        Map<String, Object> employee = new HashMap<>();
        employee.put("employeeId", 1);
        employeeDetails.add(employee);
        ResponseEntity<List<Map<String, Object>>> responseEntity = ResponseEntity.ok(employeeDetails);
        when(restTemplate.exchange(eq("http://BATCH-MANAGEMENT-SERVICE/batch/batch-details/employees/{batchId}"),
                eq(HttpMethod.GET), any(), any(ParameterizedTypeReference.class), eq(batchId)))
                .thenReturn(responseEntity);

        // Test and expect EmployeeNotFoundException
        assertThrows(EmployeeNotFoundException.class, () -> {
            attendanceReportService.generateEmployeeAttendanceReport(batchId, courseId, employeeId, startDate, endDate);
        });
    }
    

    @Test
    public void testGenerateEmployeeAttendanceReportSuccess1() {
        // Arrange
        Long batchId = 1L;
        Long courseId = 1L;
        Long employeeId = 1L;
        String date = "2024-01-01";

        // Mock employee details
        List<Map<String, Object>> employeeDetails = new ArrayList<>();
        Map<String, Object> employeeDetail = new HashMap<>();
        employeeDetail.put("employeeId", 1); // Assuming employee with ID 1 exists
        // Add other employee details as needed
        employeeDetails.add(employeeDetail);

        // Mock restTemplate exchange call to return the populated employeeDetails list
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), any(ParameterizedTypeReference.class), eq(batchId)))
                .thenReturn(new ResponseEntity<>(employeeDetails, HttpStatus.OK));

        // Mock attendance repository response
        List<Attendance> attendances = new ArrayList<>();
        // Populate attendances with attendance data

        when(attendanceRepository.findByBatchIdAndCourseIdAndUserIdAndAttendance_Date(eq(batchId), eq(courseId),
                eq(employeeId), eq(date)))
                .thenReturn(attendances);

        // Act
        List<ReportExcelDto> result = attendanceReportService.generateEmployeeAttendanceReport(batchId, courseId, employeeId, date);

        // Assert
        assertNotNull(result);
        // Add assertions for the generated report DTOs
    }
    
    
    @Test
    public void testGenerateEmployeeAttendanceExcelFileInvalidBatchId() {
        // Arrange
        Long invalidBatchId = -1L;
        Long courseId = 1L;
        Long employeeId = 1L;
        String startDate = "2024-01-01";
        String endDate = "2024-01-31";

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), any(ParameterizedTypeReference.class), eq(invalidBatchId)))
                .thenReturn(new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK)); // Mock empty response

        // Act & Assert
        assertThrows(EmployeeNotFoundException.class, () -> { // Adjusted to expect EmployeeNotFoundException
            attendanceReportService.generateEmployeeAttendanceExcelFile(invalidBatchId, courseId, employeeId, startDate, endDate);
        });
    }

 
    @Test
    void testCalculateAttendancePercentageException() {
        long batchId = 1L;
        long courseId = 1L;
        String date = "2024-05-21";

        // Mock response from restTemplate
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), any(ParameterizedTypeReference.class), anyLong()))
                .thenThrow(new RuntimeException("Internal Server Error"));

        // Assert exception
        AttendanceCalculationException exception = assertThrows(AttendanceCalculationException.class, () -> {
            attendanceReportService.calculateAttendancePercentage(batchId, courseId, date);
        });
        assertEquals("Attendance calculation failed: Internal Server Error", exception.getMessage());
    }

    
    // Helper method to create a mock ReportExcelDto
    private ReportExcelDto createMockReport(Long userId, String type, String status) {
        AttendanceDetail detail = new AttendanceDetail("2024-05-21", type, status);
        ReportExcelDto report = new ReportExcelDto();
        report.setUserId(userId);
        report.setAttendance(Collections.singletonList(detail));
        return report;
    }
    
    
}

