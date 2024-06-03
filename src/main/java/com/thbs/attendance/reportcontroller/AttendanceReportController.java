package com.thbs.attendance.reportcontroller;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thbs.attendance.exceptionhandler.AttendanceNotFoundException;
import com.thbs.attendance.reportdto.ReportExcelDto;
import com.thbs.attendance.reportservice.AttendanceReportService;
 
 
@RestController
@RequestMapping("/attendance-report")
public class AttendanceReportController {
 
	
	@Autowired
    private AttendanceReportService attendanceReportService;


	//ATTENDANCE LIST OF ALL EMPLOYEES IN A BATCH ALONG WITH COURSEID AND DATE RANGE
	@GetMapping("/report/{batchId}/{courseId}/{startDate}/{endDate}")
	public ResponseEntity<List<ReportExcelDto>> generateAttendanceReport(@PathVariable Long batchId,
			@PathVariable Long courseId, @PathVariable String startDate, @PathVariable String endDate) {

		List<ReportExcelDto> attendanceReport = attendanceReportService.generateAttendanceReportDetails(batchId,
				courseId, startDate, endDate);

		if (attendanceReport.isEmpty()) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.ok(attendanceReport);
		}
	}

	
	//ATTENDANCE LIST OF ALL EMPLOYEES IN A BATCH ALONG WITH COURSEID AND  PARTICULAR DATE 
	@GetMapping("/report/{batchId}/{courseId}/{date}")
	public ResponseEntity<List<ReportExcelDto>> generateAttendanceReport(@PathVariable Long batchId,
			@PathVariable Long courseId, @PathVariable String date) {
		List<ReportExcelDto> report = attendanceReportService.generateAttendanceReportDetails(batchId, courseId, date);
		return ResponseEntity.ok().body(report);
	}

	
	//ATTENDANCE LIST OF ALL EMPLOYEES IN A BATCH ALONG WITH COURSEID AND  DATE RANGE  IN EXCEL FORMAT
	@GetMapping("/excel/{batchId}/{courseId}/{startDate}/{endDate}")
	public ResponseEntity<FileSystemResource> downloadExcelFile(@PathVariable Long batchId, @PathVariable Long courseId,
			@PathVariable String startDate, @PathVariable String endDate) throws IOException {
		String fileName = "AttendanceReport.xlsx";
		attendanceReportService.generateAttendanceExcelFile(batchId, courseId, startDate, endDate);

		File file = new File(fileName);
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
		headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		return new ResponseEntity<>(new FileSystemResource(file), headers, HttpStatus.OK);
	}
	

	//ATTENDANCE LIST OF ALL EMPLOYEES IN A BATCH ALONG WITH COURSEID AND PARTICULAR DATE  IN EXCEL FORMAT
	@GetMapping("/excel/{batchId}/{courseId}/{date}")
	public ResponseEntity<Resource> generateAttendanceExcel(@PathVariable Long batchId, @PathVariable Long courseId,
			@PathVariable String date) {
		String fileName = "AttendanceReport.xlsx";
		try {
			// Generate the Excel file
			attendanceReportService.generateAttendanceExcelFile(batchId, courseId, date);

			// Prepare the file for download
			File file = new File(fileName);
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
			headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			Resource resource = new FileSystemResource(file);

			// Return the file for download
			return ResponseEntity.ok().headers(headers).body(resource);
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Or return an error message as
																						// needed
		}
	}

	
	//ATTENDANCE LIST OF ALL EMPLOYEES IN A BATCH ALONG WITH COURSEID ,EMPLOYEEID AND   DATE  RANGE IN JSON FORMAT
	@GetMapping("/employee/{batchId}/{courseId}/{employeeId}/{startDate}/{endDate}")
	public List<ReportExcelDto> generateEmployeeAttendanceReport(@PathVariable Long batchId,
			@PathVariable Long courseId, @PathVariable Long employeeId, @PathVariable String startDate,
			@PathVariable String endDate) {
		return attendanceReportService.generateEmployeeAttendanceReport(batchId, courseId, employeeId, startDate,
				endDate);
	}
	

	//ATTENDANCE LIST OF ALL EMPLOYEES IN A BATCH ALONG WITH COURSEID ,EMPLOYEEID AND  PARTICULAR DATE  IN JSON FORMAT
	@GetMapping("/employee/{batchId}/{courseId}/{employeeId}/{date}")
	public ResponseEntity<List<ReportExcelDto>> generateEmployeeAttendanceReport(@PathVariable Long batchId,
			@PathVariable Long courseId, @PathVariable Long employeeId, @PathVariable String date) {

		List<ReportExcelDto> attendanceReport = attendanceReportService.generateEmployeeAttendanceReport(batchId,
				courseId, employeeId, date);

		return ResponseEntity.ok(attendanceReport);
	}
	

	//ATTENDANCE LIST OF ALL EMPLOYEES IN A BATCH ALONG WITH COURSEID ,EMPLOYEEID AND  DATE RANGE  IN EXCEL FORMAT
	@GetMapping("/employee/excel/{batchId}/{courseId}/{employeeId}/{startDate}/{endDate}")
	public ResponseEntity<FileSystemResource> downloadEmployeeAttendanceExcelFile(@PathVariable Long batchId,
			@PathVariable Long courseId, @PathVariable Long employeeId, @PathVariable String startDate,
			@PathVariable String endDate) throws IOException {
		String fileName = "EmployeeAttendanceReport.xlsx";
		attendanceReportService.generateEmployeeAttendanceExcelFile(batchId, courseId, employeeId, startDate, endDate);
		File file = new File(fileName);
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
		headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		return new ResponseEntity<>(new FileSystemResource(file), headers, HttpStatus.OK);
	}
	

	//ATTENDANCE LIST OF ALL EMPLOYEES IN A BATCH ALONG WITH COURSEID ,EMPLOYEEID AND PARTICULAR  DATE   IN EXCEL FORMAT
	@GetMapping("/employee/excel/{batchId}/{courseId}/{employeeId}/{date}")
	public ResponseEntity<FileSystemResource> downloadEmployeeAttendanceExcelFile(@PathVariable Long batchId,
			@PathVariable Long courseId, @PathVariable Long employeeId, @PathVariable String date) throws IOException {
		String fileName = "EmployeeAttendanceReport.xlsx";
		attendanceReportService.generateEmployeeAttendanceExcelFile(batchId, courseId, employeeId, date);
		File file = new File(fileName);
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
		headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		return new ResponseEntity<>(new FileSystemResource(file), headers, HttpStatus.OK);
	}
	

	//ATTENDANCE PERCENTAGE ON DATE RANGE
	@GetMapping("/attendance/percentage/{batchId}/{courseId}/{startDate}/{endDate}")
	public Map<String, Map<String, Double>> getAttendancePercentage(@PathVariable Long batchId,
			@PathVariable Long courseId, @PathVariable String startDate, @PathVariable String endDate) {
		return attendanceReportService.calculateAttendancePercentage(batchId, courseId, startDate, endDate);
	}
	

	//ATTENDANCE PERCENTAGE ON PARTICULAR DATE
	@GetMapping("/attendance/percentage/{batchId}/{courseId}/{date}")
	public Map<String, Map<String, Double>> getAttendancePercentage(@PathVariable Long batchId,
			@PathVariable Long courseId, @PathVariable String date) {
		return attendanceReportService.calculateAttendancePercentage(batchId, courseId, date);
	}
    
	
	// list of all batches and courses on a specific date
	@GetMapping("/batches/{date}")
	public ResponseEntity<Map<Long, Map<String, Object>>> getBatchesAndCoursesByDate(@PathVariable("date") String date) {
	    Map<Long, Map<String, Object>> result = new HashMap<>();

	    List<Long> batchIdsWithAttendance = attendanceReportService.findBatchIdsWithAttendanceOnDate(date);
	    if (batchIdsWithAttendance.isEmpty()) {
	        throw new AttendanceNotFoundException("No attendance found for any batch on date " + date);
	    }

	    for (Long batchId : batchIdsWithAttendance) {
	        Set<Long> coursesOnDate = attendanceReportService.findCoursesOnDateForBatch(batchId, date);
	        for (Long courseId : coursesOnDate) {
	            Map<String, Object> attendanceMap = attendanceReportService.calculateAttendancePercentageWithHalfDays(batchId, courseId, date);
	            if (!attendanceMap.isEmpty()) {
	                Map<String, Object> formattedMap = new LinkedHashMap<>(); // Use LinkedHashMap to maintain insertion order
	                formattedMap.put("courseId", courseId);
	                formattedMap.putAll(attendanceMap);
	                result.put(batchId, formattedMap);
	            }
	        }
	    }

	    return ResponseEntity.ok(result);
	}

}
  
    
