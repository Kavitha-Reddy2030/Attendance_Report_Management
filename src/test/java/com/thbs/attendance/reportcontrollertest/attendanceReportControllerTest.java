package com.thbs.attendance.reportcontrollertest;

import com.thbs.attendance.reportcontroller.AttendanceReportController;
import com.thbs.attendance.reportdto.ReportExcelDto;
import com.thbs.attendance.reportservice.AttendanceReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class attendanceReportControllerTest {


    @Mock
    private AttendanceReportService attendanceReportService;

    @InjectMocks
    private AttendanceReportController attendanceReportController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    
    @Test
    public void testGenerateAttendanceReport_NoContent() {
        // Mock data
        Long batchId = 1L;
        Long courseId = 1L;
        String startDate = "2024-01-01";
        String endDate = "2024-01-31";

        // Mock service response
        when(attendanceReportService.generateAttendanceReportDetails(batchId, courseId, startDate, endDate))
                .thenReturn(new ArrayList<>());

        // Call the controller method
        ResponseEntity<List<ReportExcelDto>> response = attendanceReportController.generateAttendanceReport(batchId, courseId, startDate, endDate);

        // Verify the response
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
    
    
    @Test
    public void testGenerateAttendanceReport_WithData() {
        // Mock data
        Long batchId = 1L;
        Long courseId = 1L;
        String startDate = "2024-01-01";
        String endDate = "2024-01-31";
        List<ReportExcelDto> reportList = new ArrayList<>();
        reportList.add(new ReportExcelDto(/* add necessary data */));

        // Mock service response
        when(attendanceReportService.generateAttendanceReportDetails(batchId, courseId, startDate, endDate))
                .thenReturn(reportList);

        // Call the controller method
        ResponseEntity<List<ReportExcelDto>> response = attendanceReportController.generateAttendanceReport(batchId, courseId, startDate, endDate);

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reportList, response.getBody());
    }
    
    
    @Test
    public void testGenerateAttendanceReport() {
        // Mock data
        Long batchId = 1L;
        Long courseId = 1L;
        String date = "2024-01-01";
        List<ReportExcelDto> reportList = new ArrayList<>();
        reportList.add(new ReportExcelDto(/* add necessary data */));

        // Mock service response
        when(attendanceReportService.generateAttendanceReportDetails(batchId, courseId, date))
                .thenReturn(reportList);

        // Call the controller method
        ResponseEntity<List<ReportExcelDto>> response = attendanceReportController.generateAttendanceReport(batchId, courseId, date);

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reportList, response.getBody());
    }
    

    @Test
    public void testDownloadExcelFile() throws IOException {
        // Mock data
        Long batchId = 1L;
        Long courseId = 1L;
        String startDate = "2024-01-01";
        String endDate = "2024-01-31";
        String fileName = "AttendanceReport.xlsx";

        // Mock service call
        doNothing().when(attendanceReportService).generateAttendanceExcelFile(batchId, courseId, startDate, endDate);

        // Call the controller method
        ResponseEntity<FileSystemResource> response = attendanceReportController.downloadExcelFile(batchId, courseId, startDate, endDate);

        // Verify the service method is called
        verify(attendanceReportService).generateAttendanceExcelFile(batchId, courseId, startDate, endDate);

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(fileName, response.getHeaders().getContentDisposition().getFilename());
        assertEquals(MediaType.APPLICATION_OCTET_STREAM, response.getHeaders().getContentType());
    }
    
    
    @Test
    public void testGenerateEmployeeAttendanceReport() {
        // Mock data
        Long batchId = 1L;
        Long courseId = 1L;
        Long employeeId = 1L;
        String startDate = "2024-01-01";
        String endDate = "2024-01-31";
        List<ReportExcelDto> reportList = new ArrayList<>();
        reportList.add(new ReportExcelDto(/* add necessary data */));

        // Mock service response
        when(attendanceReportService.generateEmployeeAttendanceReport(batchId, courseId, employeeId, startDate, endDate))
                .thenReturn(reportList);

        // Call the controller method
        List<ReportExcelDto> response = attendanceReportController.generateEmployeeAttendanceReport(batchId, courseId, employeeId, startDate, endDate);

        // Verify the response
        assertEquals(reportList, response);
    }
    
    
    @Test
    public void testGenerateEmployeeAttendanceReportDateS() {
        // Mock data
        Long batchId = 1L;
        Long courseId = 1L;
        Long employeeId = 1L;
        String date = "2024-01-01";
        List<ReportExcelDto> reportList = new ArrayList<>();
        reportList.add(new ReportExcelDto(/* add necessary data */));

        // Mock service response
        when(attendanceReportService.generateEmployeeAttendanceReport(batchId, courseId, employeeId, date))
                .thenReturn(reportList);

        // Call the controller method
        ResponseEntity<List<ReportExcelDto>> response = attendanceReportController.generateEmployeeAttendanceReport(batchId, courseId, employeeId, date);

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reportList, response.getBody());
    }
    

    @Test
    public void testDownloadEmployeeAttendanceExcelFile() throws IOException {
        // Mock data
        Long batchId = 1L;
        Long courseId = 1L;
        Long employeeId = 1L;
        String startDate = "2024-01-01";
        String endDate = "2024-01-31";
        String fileName = "EmployeeAttendanceReport.xlsx";

        // Mock service call
        doNothing().when(attendanceReportService).generateEmployeeAttendanceExcelFile(batchId, courseId, employeeId, startDate, endDate);

        // Call the controller method
        ResponseEntity<FileSystemResource> response = attendanceReportController.downloadEmployeeAttendanceExcelFile(batchId, courseId, employeeId, startDate, endDate);

        // Verify the service method is called
        verify(attendanceReportService).generateEmployeeAttendanceExcelFile(batchId, courseId, employeeId, startDate, endDate);

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(fileName, response.getHeaders().getContentDisposition().getFilename());
        assertEquals(MediaType.APPLICATION_OCTET_STREAM, response.getHeaders().getContentType());
    }
    
    
    @Test
    public void testDownloadEmployeeAttendanceExcelFileDate() throws IOException {
        // Mock data
        Long batchId = 1L;
        Long courseId = 1L;
        Long employeeId = 1L;
        String date = "2024-01-01";
        String fileName = "EmployeeAttendanceReport.xlsx";

        // Mock service call
        doNothing().when(attendanceReportService).generateEmployeeAttendanceExcelFile(batchId, courseId, employeeId, date);

        // Call the controller method
        ResponseEntity<FileSystemResource> response = attendanceReportController.downloadEmployeeAttendanceExcelFile(batchId, courseId, employeeId, date);

        // Verify the service method is called
        verify(attendanceReportService).generateEmployeeAttendanceExcelFile(batchId, courseId, employeeId, date);

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(fileName, response.getHeaders().getContentDisposition().getFilename());
        assertEquals(MediaType.APPLICATION_OCTET_STREAM, response.getHeaders().getContentType());
    }
    

    @Test
    public void testGetAttendancePercentage() {
        // Mock data
        Long batchId = 1L;
        Long courseId = 1L;
        String startDate = "2024-01-01";
        String endDate = "2024-01-31";
        Map<String, Map<String, Double>> attendancePercentage = new HashMap<>();
        // Populate attendancePercentage with mock data

        // Mock service response
        when(attendanceReportService.calculateAttendancePercentage(batchId, courseId, startDate, endDate))
                .thenReturn(attendancePercentage);

        // Call the controller method
        Map<String, Map<String, Double>> result = attendanceReportController.getAttendancePercentage(batchId, courseId, startDate, endDate);

        // Verify the service method is called
        verify(attendanceReportService).calculateAttendancePercentage(batchId, courseId, startDate, endDate);

        // Verify the result
        assertEquals(attendancePercentage, result);
    }
    

    @Test
    public void testGetAttendancePercentageOnDate() {
        // Mock data
        Long batchId = 1L;
        Long courseId = 1L;
        String date = "2024-01-01";
        Map<String, Map<String, Double>> attendancePercentage = new HashMap<>();
        // Populate attendancePercentage with mock data

        // Mock service response
        when(attendanceReportService.calculateAttendancePercentage(batchId, courseId, date))
                .thenReturn(attendancePercentage);

        // Call the controller method
        Map<String, Map<String, Double>> result = attendanceReportController.getAttendancePercentage(batchId, courseId, date);

        // Verify the service method is called
        verify(attendanceReportService).calculateAttendancePercentage(batchId, courseId, date);

        // Verify the result
        assertEquals(attendancePercentage, result);
    }
    
    
}

 

   
