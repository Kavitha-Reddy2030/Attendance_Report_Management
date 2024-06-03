package com.thbs.attendance.reportservice;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.thbs.attendance.Entity.Attendance;
import com.thbs.attendance.Entity.AttendanceDetail;
import com.thbs.attendance.Repository.AttendanceRepository;
import com.thbs.attendance.exceptionhandler.AttendanceCalculationException;
import com.thbs.attendance.exceptionhandler.BatchNotFoundException;
import com.thbs.attendance.exceptionhandler.CourseNotFoundException;
import com.thbs.attendance.exceptionhandler.EmployeeNotFoundException;
import com.thbs.attendance.exceptionhandler.ExternalServiceException;
import com.thbs.attendance.exceptionhandler.InvalidDateFormatException;
import com.thbs.attendance.reportdto.AttendanceReportDetailDto;
import com.thbs.attendance.reportdto.AttendanceReportDto;
import com.thbs.attendance.reportdto.ReportExcelDto;


@Service
public class AttendanceReportService {

	
	@Autowired
	private AttendanceRepository attendanceRepository;

	@Autowired
	private RestTemplate restTemplate;
	
	
	// ATTENDANCE LIST OF ALL EMPLOYEES IN A BATCH ALONG WITH COURSEID AND DATE RANGE IN JSON FORMAT
	public List<ReportExcelDto> generateAttendanceReportDetails(Long batchId, Long courseId, String startDate,
			String endDate) {
		String BATCH_MANAGEMENT_SERVICE_URL = "http://BATCH-MANAGEMENT-SERVICE/batch/batch-details/employees/{batchId}";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		

		ResponseEntity<List<Map<String, Object>>> responseEntity;
		try {
			responseEntity = restTemplate.exchange(BATCH_MANAGEMENT_SERVICE_URL, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<Map<String, Object>>>() {
					}, batchId);
		} catch (HttpClientErrorException.NotFound e) {
			throw new BatchNotFoundException("Batch with id " + batchId + " not found");
		} catch (Exception e) {
			throw new ExternalServiceException("Error occurred while fetching batch details");
		}

		List<Map<String, Object>> employeeDetails = responseEntity.getBody();
		if (employeeDetails == null || employeeDetails.isEmpty()) {
			throw new BatchNotFoundException("No employees found for batch id " + batchId);
		}

		List<Long> employeeIds = new ArrayList<>();
		Map<Long, Map<String, Object>> employeeDetailsMap = new HashMap<>();
		for (Map<String, Object> employeeDetail : employeeDetails) {
			Integer employeeIdInteger = (Integer) employeeDetail.get("employeeId");
			Long employeeId = employeeIdInteger.longValue();
			employeeIds.add(employeeId);
			employeeDetailsMap.put(employeeId, employeeDetail);
		}

		// Fetch attendance data from repository
		List<Attendance> attendances;
		try {
			attendances = attendanceRepository.findByBatchIdAndCourseIdAndUserIdInAndAttendance_DateBetween(batchId,
					courseId, employeeIds, startDate, endDate);
		} catch (Exception e) {
			throw new AttendanceCalculationException("Error occurred while fetching attendance details");
		}

		if (attendances.isEmpty()) {
			throw new CourseNotFoundException("No attendance records found for course id " + courseId);
		}

		// Generate attendance report DTOs
		List<ReportExcelDto> attendanceReport = new ArrayList<>();
		for (Attendance attendance : attendances) {
			Map<String, Object> employeeDetail = employeeDetailsMap.get(attendance.getUserId());
			if (employeeDetail == null) {
				continue; // Skip if employee details are not found for the attendance record
			}

			String email = (String) employeeDetail.get("email");
			String name = (String) employeeDetail.get("firstName") + " " + (String) employeeDetail.get("lastName");
			String businessUnit = (String) employeeDetail.get("businessUnit");

			ReportExcelDto reportDto = new ReportExcelDto();
			reportDto.setId(attendance.getId());
			reportDto.setBatchId(attendance.getBatchId());
			reportDto.setCourseId(attendance.getCourseId());
			reportDto.setUserId(attendance.getUserId());
			reportDto.setEmail(email);
			reportDto.setName(name);
			reportDto.setBusinessUnit(businessUnit);

			List<AttendanceDetail> reportDetails = new ArrayList<>();
			LocalDate currentDate = LocalDate.parse(startDate);
			LocalDate endDateParsed = LocalDate.parse(endDate);
			while (!currentDate.isAfter(endDateParsed)) {
				String formattedDate = currentDate.format(DateTimeFormatter.ISO_DATE);
				List<AttendanceDetail> detailsForDate = attendance.getAttendance().stream()
						.filter(detail -> detail.getDate().equals(formattedDate)).collect(Collectors.toList());

				for (AttendanceDetail detail : detailsForDate) {
					AttendanceDetail detailDto = new AttendanceDetail();
					detailDto.setDate(formattedDate);
					detailDto.setType(detail.getType());
					detailDto.setStatus(detail.getStatus());
					reportDetails.add(detailDto);
				}

				// If no detail is found for the date, mark it as absent
				if (detailsForDate.isEmpty()) {
					AttendanceDetail detailDto = new AttendanceDetail();
					detailDto.setDate(formattedDate);
					detailDto.setType("Not available");
					detailDto.setStatus("Not Taken");
					reportDetails.add(detailDto);
				}

				currentDate = currentDate.plusDays(1); // Move to the next date

			}
			reportDto.setAttendance(reportDetails);
			attendanceReport.add(reportDto);
		}
		return attendanceReport;
	}
	

	//ATTENDANCE LIST OF ALL EMPLOYEES IN A BATCH ALONG WITH COURSEID AND  PARTICULAR DATE IN JSON FORMAT
	public List<ReportExcelDto> generateAttendanceReportDetails(Long batchId, Long courseId, String date) {
		String BATCH_MANAGEMENT_SERVICE_URL = "http://BATCH-MANAGEMENT-SERVICE/batch/batch-details/employees/{batchId}";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<List<Map<String, Object>>> responseEntity;
		try {
			responseEntity = restTemplate.exchange(BATCH_MANAGEMENT_SERVICE_URL, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<Map<String, Object>>>() {
					}, batchId);
		} catch (HttpClientErrorException.NotFound e) {
			throw new BatchNotFoundException("Batch with id " + batchId + " not found");
		} catch (Exception e) {
			throw new ExternalServiceException("Error occurred while fetching batch details");
		}

		List<Map<String, Object>> employeeDetails = responseEntity.getBody();
		if (employeeDetails == null || employeeDetails.isEmpty()) {
			throw new BatchNotFoundException("No employees found for batch id " + batchId);
		}

		List<Long> employeeIds = new ArrayList<>();
		Map<Long, Map<String, Object>> employeeDetailsMap = new HashMap<>();
		for (Map<String, Object> employeeDetail : employeeDetails) {
			Integer employeeIdInteger = (Integer) employeeDetail.get("employeeId");
			Long employeeId = employeeIdInteger.longValue();
			employeeIds.add(employeeId);
			employeeDetailsMap.put(employeeId, employeeDetail);
		}

		// Validate date format
		LocalDate parsedDate;
		try {
			parsedDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
		} catch (DateTimeParseException e) {
			throw new InvalidDateFormatException("Invalid date format: " + date);
		}

		// Fetch attendance data from repository for the specific date
		List<Attendance> attendances;
		try {
			attendances = attendanceRepository.findByBatchIdAndCourseIdAndUserIdInAndAttendance_Date(batchId, courseId,
					employeeIds, date);
		} catch (Exception e) {
			throw new AttendanceCalculationException("Error occurred while fetching attendance details");
		}

		if (attendances.isEmpty()) {
			throw new CourseNotFoundException("No attendance records found for course id " + courseId);
		}

		// Generate attendance report DTOs
		List<ReportExcelDto> attendanceReport = new ArrayList<>();
		for (Attendance attendance : attendances) {
			Map<String, Object> employeeDetail = employeeDetailsMap.get(attendance.getUserId());
			if (employeeDetail == null) {
				continue; // Skip if employee details are not found for the attendance record
			}

			String email = (String) employeeDetail.get("email");
			String name = (String) employeeDetail.get("firstName") + " " + (String) employeeDetail.get("lastName");
			String businessUnit = (String) employeeDetail.get("businessUnit");

			ReportExcelDto reportDto = new ReportExcelDto();
			reportDto.setId(attendance.getId());
			reportDto.setBatchId(attendance.getBatchId());
			reportDto.setCourseId(attendance.getCourseId());
			reportDto.setUserId(attendance.getUserId());
			reportDto.setEmail(email);
			reportDto.setName(name);
			reportDto.setBusinessUnit(businessUnit);

			List<AttendanceDetail> reportDetails = new ArrayList<>();
			for (AttendanceDetail detail : attendance.getAttendance()) {
				if (detail.getDate().equals(date)) {
					AttendanceDetail detailDto = new AttendanceDetail();
					detailDto.setDate(date);
					detailDto.setType(detail.getType());
					detailDto.setStatus(detail.getStatus());
					reportDetails.add(detailDto);
				}
			}

			// If no detail is found for the date, mark it as absent
			if (reportDetails.isEmpty()) {
				AttendanceDetail detailDto = new AttendanceDetail();
				detailDto.setDate(date);
				detailDto.setType("Not available");
				detailDto.setStatus("Not Taken");
				reportDetails.add(detailDto);
			}

			reportDto.setAttendance(reportDetails);
			attendanceReport.add(reportDto);
		}
		return attendanceReport;
	}
	
	
	//ATTENDANCE LIST OF ALL EMPLOYEES IN A BATCH ALONG WITH COURSEID AND DATE RANGE IN EXCEL FORMAT
	public void generateAttendanceExcelFile(Long batchId, Long courseId, String startDate, String endDate)
			throws IOException {
		try {

			List<ReportExcelDto> reportData = generateAttendanceReportDetails(batchId, courseId, startDate, endDate);
			try (XSSFWorkbook workbook = new XSSFWorkbook()) {
				Sheet sheet = workbook.createSheet("Attendance Report");

				// Create header row for dates
				Row dateHeaderRow = sheet.createRow(0);
				Row typeHeaderRow = sheet.createRow(1);

				// Create header cells for Sl No, User Id, Name, Email Id, Business Unit
				createCell(dateHeaderRow, 0, "Sl No");
				createCell(dateHeaderRow, 1, "User Id");
				createCell(dateHeaderRow, 2, "Name");
				createCell(dateHeaderRow, 3, "Email Id");
				createCell(dateHeaderRow, 4, "Business Unit");

				// Track column index for dates
				int columnIdx = 5;

				// Map to store the column index for each date
				Map<String, Integer> dateColumnMap = new HashMap<>();

				for (ReportExcelDto report : reportData) {
					for (AttendanceDetail detail : report.getAttendance()) {
						String dateKey = detail.getDate();
						// Check if the date exists in the map
						if (!dateColumnMap.containsKey(dateKey)) {
							// If not, create a new entry for the date and types
							dateColumnMap.put(dateKey, columnIdx);
							// Create the main date cell
							Cell dateCell = dateHeaderRow.createCell(columnIdx);
							dateCell.setCellValue(detail.getDate());
							// Merge the date cell with the next two cells
							CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, columnIdx, columnIdx + 1);
							sheet.addMergedRegion(cellRangeAddress);
							columnIdx += 2;
						}
					}
				}

				// Create subheader row for types
				for (Map.Entry<String, Integer> entry : dateColumnMap.entrySet()) {
					int colIdx = entry.getValue();
					// Create subheaders for first half and second half under the date cell
					Cell firstHalfCell = typeHeaderRow.createCell(colIdx);
					firstHalfCell.setCellValue("First Half");
					Cell secondHalfCell = typeHeaderRow.createCell(colIdx + 1);
					secondHalfCell.setCellValue("Second Half");
				}

				// Create data rows
				int rowNum = 2; // Start from row 2 after the header rows
				for (ReportExcelDto report : reportData) {
					Row row = sheet.createRow(rowNum++);
					createCell(row, 0, rowNum - 2); // Sl No
					createCell(row, 1, report.getUserId()); // User Id
					createCell(row, 2, report.getName()); // Name
					createCell(row, 3, report.getEmail()); // Email Id
					createCell(row, 4, report.getBusinessUnit()); // Business Unit

					// Iterate through dateColumnMap to populate attendance status
					for (Map.Entry<String, Integer> dateEntry : dateColumnMap.entrySet()) {
						String dateKey = dateEntry.getKey();
						int colIdx = dateEntry.getValue();

						String firstHalfStatus = "Not taken";
						String secondHalfStatus = "Not taken";

						for (AttendanceDetail detail : report.getAttendance()) {
							if (detail.getDate().equals(dateKey)) {
								if (detail.getType().equalsIgnoreCase("first half")) {
									firstHalfStatus = detail.getStatus();
								} else if (detail.getType().equalsIgnoreCase("second half")) {
									secondHalfStatus = detail.getStatus();
								} else if (detail.getType().equalsIgnoreCase("full day")) {
									firstHalfStatus = detail.getStatus();
									secondHalfStatus = detail.getStatus();
								}
							}
						}

						createCell(row, colIdx, firstHalfStatus); // First Half
						createCell(row, colIdx + 1, secondHalfStatus); // Second Half
					}
				}

				// Autosize columns for better readability
				for (int i = 0; i < columnIdx; i++) {
					sheet.autoSizeColumn(i);
				}

				// Save workbook to a file
				String fileName = "AttendanceReport.xlsx";
				try (FileOutputStream fileOut = new FileOutputStream(fileName)) {
					workbook.write(fileOut);
				}
			}
		} catch (BatchNotFoundException e) {
			// Handle batch not found exception
			throw new BatchNotFoundException("No employees found for batch id " + batchId);
		} catch (CourseNotFoundException e) {
			// Handle course not found exception
			throw new CourseNotFoundException("No attendance records found for course id " + courseId);
		}
	}

	private String getAttendanceStatus(ReportExcelDto report, String date, String type) {
		for (AttendanceDetail detail : report.getAttendance()) {
			if (detail.getDate().equals(date) && detail.getType().equalsIgnoreCase(type)) {
				return detail.getStatus();
			}
		}
		return "Not taken"; // For "full day", return an empty string
	}

	private Cell createCell(Row row, int columnIdx, Object value) {
		Cell cell = row.createCell(columnIdx);

		if (value instanceof String) {
			cell.setCellValue((String) value);
		} else if (value instanceof Long) {
			cell.setCellValue((Long) value);
		} else if (value instanceof Integer) {
			cell.setCellValue((Integer) value);
		} else if (value instanceof Double) {
			cell.setCellValue((Double) value);
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else if (value instanceof LocalDate) {
			cell.setCellValue(((LocalDate) value).format(DateTimeFormatter.ISO_DATE));
		}
		return cell;
	}
	
	
	//ATTENDANCE LIST OF ALL EMPLOYEES IN A BATCH ALONG WITH COURSEID AND PARTICULAR DATE IN EXCEL FORMAT
	public void generateAttendanceExcelFile(Long batchId, Long courseId, String date) throws IOException {
		try {
			List<ReportExcelDto> reportData = generateAttendanceReportDetails(batchId, courseId, date);
			try (XSSFWorkbook workbook = new XSSFWorkbook()) {
				Sheet sheet = workbook.createSheet("Attendance Report");

				// Create header row for dates
				Row dateHeaderRow = sheet.createRow(0);
				Row typeHeaderRow = sheet.createRow(1);

				// Create header cells for Sl No, User Id, Name, Email Id, Business Unit
				createCell(dateHeaderRow, 0, "Sl No");
				createCell(dateHeaderRow, 1, "User Id");
				createCell(dateHeaderRow, 2, "Name");
				createCell(dateHeaderRow, 3, "Email Id");
				createCell(dateHeaderRow, 4, "Business Unit");

				// Track column index for dates
				int columnIdx = 5;

				// Map to store the column index for each date
				Map<String, Integer> dateColumnMap = new HashMap<>();

				for (ReportExcelDto report : reportData) {
					for (AttendanceDetail detail : report.getAttendance()) {
						String dateKey = detail.getDate();
						// Check if the date exists in the map
						if (!dateColumnMap.containsKey(dateKey)) {
							// If not, create a new entry for the date and types
							dateColumnMap.put(dateKey, columnIdx);
							// Create the main date cell
							Cell dateCell = dateHeaderRow.createCell(columnIdx);
							dateCell.setCellValue(detail.getDate());
							// Merge the date cell with the next two cells
							CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, columnIdx, columnIdx + 1);
							sheet.addMergedRegion(cellRangeAddress);
							columnIdx += 2;
						}
					}
				}

				// Create subheader row for types
				for (Map.Entry<String, Integer> entry : dateColumnMap.entrySet()) {
					int colIdx = entry.getValue();
					// Create subheaders for first half and second half under the date cell
					Cell firstHalfCell = typeHeaderRow.createCell(colIdx);
					firstHalfCell.setCellValue("First Half");
					Cell secondHalfCell = typeHeaderRow.createCell(colIdx + 1);
					secondHalfCell.setCellValue("Second Half");
				}

				// Create data rows
				int rowNum = 2; // Start from row 2 after the header rows
				for (ReportExcelDto report : reportData) {
					Row row = sheet.createRow(rowNum++);
					createCell(row, 0, rowNum - 2); // Sl No
					createCell(row, 1, report.getUserId()); // User Id
					createCell(row, 2, report.getName()); // Name
					createCell(row, 3, report.getEmail()); // Email Id
					createCell(row, 4, report.getBusinessUnit()); // Business Unit

					// Iterate through dateColumnMap to populate attendance status
					for (Map.Entry<String, Integer> dateEntry : dateColumnMap.entrySet()) {
						String dateKey = dateEntry.getKey();
						int colIdx = dateEntry.getValue();

						String firstHalfStatus = "Not taken";
						String secondHalfStatus = "Not taken";

						for (AttendanceDetail detail : report.getAttendance()) {
							if (detail.getDate().equals(dateKey)) {
								if (detail.getType().equalsIgnoreCase("first half")) {
									firstHalfStatus = detail.getStatus();
								} else if (detail.getType().equalsIgnoreCase("second half")) {
									secondHalfStatus = detail.getStatus();
								} else if (detail.getType().equalsIgnoreCase("full day")) {
									firstHalfStatus = detail.getStatus();
									secondHalfStatus = detail.getStatus();
								}
							}
						}

						createCell(row, colIdx, firstHalfStatus); // First Half
						createCell(row, colIdx + 1, secondHalfStatus); // Second Half
					}
				}

				// Autosize columns for better readability
				for (int i = 0; i < columnIdx; i++) {
					sheet.autoSizeColumn(i);
				}

				// Save workbook to a file
				String fileName = "AttendanceReport.xlsx";
				try (FileOutputStream fileOut = new FileOutputStream(fileName)) {
					workbook.write(fileOut);
				}
			}
		} catch (BatchNotFoundException e) {
			// Handle batch not found exception
			throw new BatchNotFoundException("No employees found for batch id " + batchId);
		} catch (CourseNotFoundException e) {
			// Handle course not found exception
			throw new CourseNotFoundException("No attendance records found for course id " + courseId);
		}
	}	
	
	
	//ATTENDANCE LIST OF ALL EMPLOYEES IN A BATCH ALONG WITH COURSEID ,EMPLOYEEID AND  DATE RANGE  IN JSON FORMAT
	public List<ReportExcelDto> generateEmployeeAttendanceReport(Long batchId, Long courseId, Long employeeId,
			String startDate, String endDate) {
		try {
			String BATCH_MANAGEMENT_SERVICE_URL = "http://BATCH-MANAGEMENT-SERVICE/batch/batch-details/employees/{batchId}";

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			ResponseEntity<List<Map<String, Object>>> responseEntity = restTemplate.exchange(
					BATCH_MANAGEMENT_SERVICE_URL, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<Map<String, Object>>>() {
					}, batchId);
			List<Map<String, Object>> employeeDetails = responseEntity.getBody();

			List<Long> employeeIds = new ArrayList<>();
			Map<Long, Map<String, Object>> employeeDetailsMap = new HashMap<>();
			for (Map<String, Object> employeeDetail : employeeDetails) {
				Integer empIdInteger = (Integer) employeeDetail.get("employeeId");
				Long empId = empIdInteger.longValue();
				employeeIds.add(empId);
				employeeDetailsMap.put(empId, employeeDetail);
			}

			// Check if the specified employee is part of the batch
			if (!employeeIds.contains(employeeId)) {
				throw new EmployeeNotFoundException("Employee with ID " + employeeId + " not found");
			}

			// Fetch attendance data from repository for the specified employee
			List<Attendance> attendances = attendanceRepository
					.findByBatchIdAndCourseIdAndUserIdAndAttendance_DateBetween(batchId, courseId, employeeId,
							startDate, endDate);

			// Generate attendance report DTOs
			List<ReportExcelDto> attendanceReport = new ArrayList<>();
			for (Attendance attendance : attendances) {
				Map<String, Object> employeeDetail = employeeDetailsMap.get(attendance.getUserId());
				String email = (String) employeeDetail.get("email");
				String name = (String) employeeDetail.get("firstName") + " " + (String) employeeDetail.get("lastName");
				String businessUnit = (String) employeeDetail.get("businessUnit");

				ReportExcelDto reportDto = new ReportExcelDto();
				reportDto.setId(attendance.getId());
				reportDto.setBatchId(attendance.getBatchId());
				reportDto.setCourseId(attendance.getCourseId());
				reportDto.setUserId(attendance.getUserId());
				reportDto.setEmail(email);
				reportDto.setName(name);
				reportDto.setBusinessUnit(businessUnit);

				List<AttendanceDetail> reportDetails = new ArrayList<>();
				LocalDate currentDate = LocalDate.parse(startDate);
				LocalDate endDateParsed = LocalDate.parse(endDate);
				while (!currentDate.isAfter(endDateParsed)) {
					String formattedDate = currentDate.format(DateTimeFormatter.ISO_DATE);
					List<AttendanceDetail> detailsForDate = attendance.getAttendance().stream()
							.filter(detail -> detail.getDate().equals(formattedDate)).collect(Collectors.toList());

					for (AttendanceDetail detail : detailsForDate) {

						AttendanceDetail detailDto = new AttendanceDetail();
						detailDto.setDate(formattedDate);
						detailDto.setType(detail.getType());
						detailDto.setStatus(detail.getStatus());
						reportDetails.add(detailDto);
					}

					// If no detail is found for the date, mark it as absent
					if (detailsForDate.isEmpty()) {
						AttendanceDetail detailDto = new AttendanceDetail();
						detailDto.setDate(formattedDate);
						detailDto.setType("Not available");
						detailDto.setStatus("Not Taken");
						reportDetails.add(detailDto);
					}

					currentDate = currentDate.plusDays(1); // Move to the next date
				}
				reportDto.setAttendance(reportDetails);
				attendanceReport.add(reportDto);
			}

			return attendanceReport;
		} catch (HttpClientErrorException.NotFound e) {
			if (e.getMessage().contains("Batch")) {
				throw new BatchNotFoundException("Batch with ID " + batchId + " not found");
			} else if (e.getMessage().contains("Course")) {
				throw new CourseNotFoundException("Course with ID " + courseId + " not found");
			} else {
				throw e;
			}
		}
	}
	

	//ATTENDANCE LIST OF ALL EMPLOYEES IN A BATCH ALONG WITH COURSEID ,EMPLOYEEID AND  PARTICULAR DATE  IN JSON FORMAT
	public List<ReportExcelDto> generateEmployeeAttendanceReport(Long batchId, Long courseId, Long employeeId,
			String date) {
		try {
			String BATCH_MANAGEMENT_SERVICE_URL = "http://BATCH-MANAGEMENT-SERVICE/batch/batch-details/employees/{batchId}";

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			ResponseEntity<List<Map<String, Object>>> responseEntity = restTemplate.exchange(
					BATCH_MANAGEMENT_SERVICE_URL, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<Map<String, Object>>>() {
					}, batchId);
			List<Map<String, Object>> employeeDetails = responseEntity.getBody();

			if (employeeDetails.isEmpty()) {
				throw new BatchNotFoundException("Batch with ID " + batchId + " not found");
			}

			List<Long> employeeIds = new ArrayList<>();
			Map<Long, Map<String, Object>> employeeDetailsMap = new HashMap<>();
			for (Map<String, Object> employeeDetail : employeeDetails) {
				Integer empIdInteger = (Integer) employeeDetail.get("employeeId");
				Long empId = empIdInteger.longValue();
				employeeIds.add(empId);
				employeeDetailsMap.put(empId, employeeDetail);
			}

			// Check if the specified employee is part of the batch
			if (!employeeIds.contains(employeeId)) {
				throw new EmployeeNotFoundException("Employee with ID " + employeeId + " not found");
			}

			// Fetch attendance data from repository for the specified employee and date
			List<Attendance> attendances = attendanceRepository
					.findByBatchIdAndCourseIdAndUserIdAndAttendance_Date(batchId, courseId, employeeId, date);

			// Generate attendance report DTOs
			List<ReportExcelDto> attendanceReport = new ArrayList<>();
			for (Attendance attendance : attendances) {
				Map<String, Object> employeeDetail = employeeDetailsMap.get(attendance.getUserId());
				String email = (String) employeeDetail.get("email");
				String name = (String) employeeDetail.get("firstName") + " " + (String) employeeDetail.get("lastName");
				String businessUnit = (String) employeeDetail.get("businessUnit");

				ReportExcelDto reportDto = new ReportExcelDto();
				reportDto.setId(attendance.getId());
				reportDto.setBatchId(attendance.getBatchId());
				reportDto.setCourseId(attendance.getCourseId());
				reportDto.setUserId(attendance.getUserId());
				reportDto.setEmail(email);
				reportDto.setName(name);
				reportDto.setBusinessUnit(businessUnit);

				List<AttendanceDetail> reportDetails = new ArrayList<>();
				String formattedDate = LocalDate.parse(date).format(DateTimeFormatter.ISO_DATE);
				List<AttendanceDetail> detailsForDate = attendance.getAttendance().stream()
						.filter(detail -> detail.getDate().equals(formattedDate)).collect(Collectors.toList());

				for (AttendanceDetail detail : detailsForDate) {
					AttendanceDetail detailDto = new AttendanceDetail();
					detailDto.setDate(formattedDate);
					detailDto.setType(detail.getType());
					detailDto.setStatus(detail.getStatus());
					reportDetails.add(detailDto);
				}

				// If no detail is found for the date, mark it as absent
				if (detailsForDate.isEmpty()) {
					AttendanceDetail detailDto = new AttendanceDetail();
					detailDto.setDate(formattedDate);
					detailDto.setType("Not available");
					detailDto.setStatus("Not Taken");
					reportDetails.add(detailDto);
				}

				reportDto.setAttendance(reportDetails);
				attendanceReport.add(reportDto);
			}

			return attendanceReport;
		} catch (HttpClientErrorException.NotFound e) {
			if (e.getMessage().contains("Batch")) {
				throw new BatchNotFoundException("Batch with ID " + batchId + " not found");
			} else if (e.getMessage().contains("Employee")) {
				throw new EmployeeNotFoundException("Employee with ID " + employeeId + " not found");
			} else {
				throw e;
			}
		}
	}

	
	//ATTENDANCE LIST OF ALL EMPLOYEES IN A BATCH ALONG WITH COURSEID ,EMPLOYEEID AND  DATE RANGE  IN EXCEL FILE
	public void generateEmployeeAttendanceExcelFile(Long batchId, Long courseId, Long employeeId, String startDate,
			String endDate) throws IOException {
		try {

			List<ReportExcelDto> reportData = generateEmployeeAttendanceReport(batchId, courseId, employeeId, startDate,
					endDate);
			try (XSSFWorkbook workbook = new XSSFWorkbook()) {
				Sheet sheet = workbook.createSheet("Employee Attendance Report");

				// Create header row for dates
				Row dateHeaderRow = sheet.createRow(0);
				Row typeHeaderRow = sheet.createRow(1);

				// Create header cells for Sl No, User Id, Name, Email Id, Business Unit
				createCell(dateHeaderRow, 0, "Sl No");
				createCell(dateHeaderRow, 1, "User Id");
				createCell(dateHeaderRow, 2, "Name");
				createCell(dateHeaderRow, 3, "Email Id");
				createCell(dateHeaderRow, 4, "Business Unit");

				// Track column index for dates
				int columnIdx = 5;

				// Map to store the column index for each date
				Map<String, Integer> dateColumnMap = new HashMap<>();

				for (ReportExcelDto report : reportData) {
					for (AttendanceDetail detail : report.getAttendance()) {
						String dateKey = detail.getDate();
						// Check if the date exists in the map
						if (!dateColumnMap.containsKey(dateKey)) {
							// If not, create a new entry for the date and types
							dateColumnMap.put(dateKey, columnIdx);
							// Create the main date cell
							Cell dateCell = dateHeaderRow.createCell(columnIdx);
							dateCell.setCellValue(detail.getDate());
							// Merge the date cell with the next two cells
							CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, columnIdx, columnIdx + 1);
							sheet.addMergedRegion(cellRangeAddress);
							columnIdx += 2;
						}
					}
				}

				// Create subheader row for types
				for (Map.Entry<String, Integer> entry : dateColumnMap.entrySet()) {
					int colIdx = entry.getValue();
					// Create subheaders for first half and second half under the date cell
					Cell firstHalfCell = typeHeaderRow.createCell(colIdx);
					firstHalfCell.setCellValue("First Half");
					Cell secondHalfCell = typeHeaderRow.createCell(colIdx + 1);
					secondHalfCell.setCellValue("Second Half");
				}

				// Create data rows
				int rowNum = 2; // Start from row 2 after the header rows
				for (ReportExcelDto report : reportData) {
					Row row = sheet.createRow(rowNum++);
					createCell(row, 0, rowNum - 2); // Sl No
					createCell(row, 1, report.getUserId()); // User Id
					createCell(row, 2, report.getName()); // Name
					createCell(row, 3, report.getEmail()); // Email Id
					createCell(row, 4, report.getBusinessUnit()); // Business Unit

					// Reset the column index for attendance details
					columnIdx = 5;

					// Iterate through dateColumnMap to populate attendance status
					for (Map.Entry<String, Integer> dateEntry : dateColumnMap.entrySet()) {
						String dateKey = dateEntry.getKey();
						int colIdx = dateEntry.getValue();

						String firstHalfStatus = "Not taken";
						String secondHalfStatus = "Not taken";

						for (AttendanceDetail detail : report.getAttendance()) {
							if (detail.getDate().equals(dateKey)) {
								if (detail.getType().equalsIgnoreCase("first half")) {
									firstHalfStatus = detail.getStatus();
								} else if (detail.getType().equalsIgnoreCase("second half")) {
									secondHalfStatus = detail.getStatus();
								} else if (detail.getType().equalsIgnoreCase("full day")) {
									firstHalfStatus = detail.getStatus();
									secondHalfStatus = detail.getStatus();
								}
							}
						}

						createCell(row, colIdx, firstHalfStatus); // First Half
						createCell(row, colIdx + 1, secondHalfStatus); // Second Half
					}
				}

				// Autosize columns for better readability
				for (int i = 0; i < columnIdx; i++) {
					sheet.autoSizeColumn(i);
				}

				// Save workbook to a file
				String fileName = "EmployeeAttendanceReport.xlsx";
				FileOutputStream fileOut = new FileOutputStream(fileName);
				workbook.write(fileOut);
				fileOut.close();
			}
		} catch (IllegalArgumentException e) {
			if (e.getMessage().contains("batch")) {
				throw new IllegalArgumentException("Invalid batch ID: " + batchId);
			} else if (e.getMessage().contains("course")) {
				throw new IllegalArgumentException("Invalid course ID: " + courseId);
			} else if (e.getMessage().contains("employee")) {
				throw new IllegalArgumentException("Invalid employee ID: " + employeeId);
			}
			throw new IllegalArgumentException("Invalid input parameters: " + e.getMessage());
		}
	}
	
	 
	//ATTENDANCE LIST OF ALL EMPLOYEES IN A BATCH ALONG WITH COURSEID ,EMPLOYEEID AND PARTICULAR  DATE   IN EXCEL FORMAT
	public void generateEmployeeAttendanceExcelFile(Long batchId, Long courseId, Long employeeId, String date)
			throws IOException {
		try {
			List<ReportExcelDto> reportData = generateEmployeeAttendanceReport(batchId, courseId, employeeId, date);
			try (XSSFWorkbook workbook = new XSSFWorkbook()) {
				Sheet sheet = workbook.createSheet("Employee Attendance Report");

				// Create header row for dates
				Row dateHeaderRow = sheet.createRow(0);
				Row typeHeaderRow = sheet.createRow(1);

				// Create header cells for Sl No, User Id, Name, Email Id, Business Unit
				createCell(dateHeaderRow, 0, "Sl No");
				createCell(dateHeaderRow, 1, "User Id");
				createCell(dateHeaderRow, 2, "Name");
				createCell(dateHeaderRow, 3, "Email Id");
				createCell(dateHeaderRow, 4, "Business Unit");

				// Track column index for dates
				int columnIdx = 5;

				// Map to store the column index for each date
				Map<String, Integer> dateColumnMap = new HashMap<>();

				for (ReportExcelDto report : reportData) {
					for (AttendanceDetail detail : report.getAttendance()) {
						String dateKey = detail.getDate();
						// Check if the date exists in the map
						if (!dateColumnMap.containsKey(dateKey)) {
							// If not, create a new entry for the date and types
							dateColumnMap.put(dateKey, columnIdx);
							// Create the main date cell
							Cell dateCell = dateHeaderRow.createCell(columnIdx);
							dateCell.setCellValue(detail.getDate());
							// Merge the date cell with the next two cells
							CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, columnIdx, columnIdx + 1);
							sheet.addMergedRegion(cellRangeAddress);
							columnIdx += 2;
						}
					}
				}

				// Create subheader row for types
				for (Map.Entry<String, Integer> entry : dateColumnMap.entrySet()) {
					int colIdx = entry.getValue();
					// Create subheaders for first half and second half under the date cell
					Cell firstHalfCell = typeHeaderRow.createCell(colIdx);
					firstHalfCell.setCellValue("First Half");
					Cell secondHalfCell = typeHeaderRow.createCell(colIdx + 1);
					secondHalfCell.setCellValue("Second Half");
				}

				// Create data rows
				int rowNum = 2; // Start from row 2 after the header rows
				for (ReportExcelDto report : reportData) {
					Row row = sheet.createRow(rowNum++);
					createCell(row, 0, rowNum - 2); // Sl No
					createCell(row, 1, report.getUserId()); // User Id
					createCell(row, 2, report.getName()); // Name
					createCell(row, 3, report.getEmail()); // Email Id
					createCell(row, 4, report.getBusinessUnit()); // Business Unit

					// Reset the column index for attendance details
					columnIdx = 5;

					// Iterate through dateColumnMap to populate attendance status
					for (Map.Entry<String, Integer> dateEntry : dateColumnMap.entrySet()) {
						String dateKey = dateEntry.getKey();
						int colIdx = dateEntry.getValue();

						String firstHalfStatus = "Not taken";
						String secondHalfStatus = "Not taken";

						for (AttendanceDetail detail : report.getAttendance()) {
							if (detail.getDate().equals(dateKey)) {
								if (detail.getType().equalsIgnoreCase("first half")) {
									firstHalfStatus = detail.getStatus();
								} else if (detail.getType().equalsIgnoreCase("second half")) {
									secondHalfStatus = detail.getStatus();
								} else if (detail.getType().equalsIgnoreCase("full day")) {
									firstHalfStatus = detail.getStatus();
									secondHalfStatus = detail.getStatus();
								}
							}
						}

						createCell(row, colIdx, firstHalfStatus); // First Half
						createCell(row, colIdx + 1, secondHalfStatus); // Second Half
					}
				}

				// Autosize columns for better readability
				for (int i = 0; i < columnIdx; i++) {
					sheet.autoSizeColumn(i);
				}

				// Save workbook to a file
				String fileName = "EmployeeAttendanceReport.xlsx";
				FileOutputStream fileOut = new FileOutputStream(fileName);
				workbook.write(fileOut);
				fileOut.close();
			}
		} catch (IllegalArgumentException e) {
			if (e.getMessage().contains("batch")) {
				throw new IllegalArgumentException("Invalid batch ID: " + batchId);
			} else if (e.getMessage().contains("course")) {
				throw new IllegalArgumentException("Invalid course ID: " + courseId);
			} else if (e.getMessage().contains("employee")) {
				throw new IllegalArgumentException("Invalid employee ID: " + employeeId);
			}
			throw new IllegalArgumentException("Invalid input parameters: " + e.getMessage());
		}
	}
	
	
	//ATTENDANCE PERCENTAGE ON  PARTICULAR DATE 
	public Map<String, Map<String, Double>> calculateAttendancePercentage(Long batchId, Long courseId, String date) {
			try {
	 
				String BATCH_MANAGEMENT_SERVICE_URL = "http://BATCH-MANAGEMENT-SERVICE/batch/batch-details/employees/{batchId}";
	 
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON); 
	 
				ResponseEntity<List<Map<String, Object>>> responseEntity = restTemplate.exchange(
						BATCH_MANAGEMENT_SERVICE_URL, HttpMethod.GET, null,
						new ParameterizedTypeReference<List<Map<String, Object>>>() {
						}, batchId);
				List<Map<String, Object>> employeeDetails = responseEntity.getBody();
				long totalEmployeesForTypeOnDate = employeeDetails.size();
//				System.out.println(totalEmployeesForTypeOnDate);
	 
				List<ReportExcelDto> reportData = generateAttendanceReportDetails(batchId, courseId, date);
	 
				Map<String, Map<String, Double>> dateAttendanceMap = new HashMap<>();
				for (ReportExcelDto report : reportData) {
					for (AttendanceDetail detail : report.getAttendance()) {
						if (!detail.getType().equalsIgnoreCase("Not available")) {
							String date1 = detail.getDate();
							String type = detail.getType();
	 
							// Check if the attendance is full day, then split into first and second half
							if (detail.getStatus().equalsIgnoreCase("Present")
									&& detail.getType().equalsIgnoreCase("Full Day")) {
								long presentEmployeesForFullDay = reportData.stream()
										.filter(r -> r.getAttendance().stream()
												.anyMatch(d -> d.getDate().equals(date1)
														&& d.getType().equalsIgnoreCase("Full Day")
														&& d.getStatus().equalsIgnoreCase("Present")))
										.count();
	 
								// Calculate the attendance percentage for full day
								double attendancePercentageForFullDay = (presentEmployeesForFullDay * 100.0)
										/ totalEmployeesForTypeOnDate;
	 
								// Update the attendance percentage for the date and types
								if (!dateAttendanceMap.containsKey(date1)) {
									dateAttendanceMap.put(date1, new HashMap<>());
								}
								dateAttendanceMap.get(date1).put("First Half", attendancePercentageForFullDay);
								dateAttendanceMap.get(date1).put("Second Half", attendancePercentageForFullDay);
							} else {
								long presentEmployeesForTypeOnDate = reportData.stream()
										.filter(r -> r.getAttendance().stream().anyMatch(
												d -> d.getDate().equals(date1) && d.getType().equalsIgnoreCase(type)
														&& d.getStatus().equalsIgnoreCase("Present")))
										.count();
//								System.out.println(presentEmployeesForTypeOnDate);
	 
								double attendancePercentageForTypeOnDate = (presentEmployeesForTypeOnDate * 100.0)
										/ totalEmployeesForTypeOnDate;
	 
								// Update the attendance percentage for the date and type
								if (!dateAttendanceMap.containsKey(date1)) {
									dateAttendanceMap.put(date1, new HashMap<>());
								}
								dateAttendanceMap.get(date1).put(type, attendancePercentageForTypeOnDate);
							}
						}
					}
				}
	 
				// Remove "Full Day" entries from the final result and set -1.0 if missing
				for (String dateKey : dateAttendanceMap.keySet()) {
					Map<String, Double> typeMap = dateAttendanceMap.get(dateKey);
					typeMap.remove("Full Day");
					if (!typeMap.containsKey("First Half")) {
						typeMap.put("First Half", -1.0); // Special value to indicate "Not Taken"
					}
					if (!typeMap.containsKey("Second Half")) {
						typeMap.put("Second Half", -1.0); // Special value to indicate "Not Taken"
					}
				}
	 
				return dateAttendanceMap;
			} catch (HttpClientErrorException.NotFound e) {
				if (e.getMessage().contains("batch")) {
					throw new BatchNotFoundException("Batch not found with ID: " + batchId);
				} else if (e.getMessage().contains("course")) {
					throw new CourseNotFoundException("Course not found with ID: " + courseId);
				}
				throw new AttendanceCalculationException("Attendance calculation failed: " + e.getMessage());
			} catch (Exception e) {
				throw new AttendanceCalculationException("Attendance calculation failed: " + e.getMessage());
			}
	}
	
	
	//ATTENDANCE PERCENTAGE ON DATE RANGE
	public Map<String, Map<String, Double>> calculateAttendancePercentage(Long batchId, Long courseId, String startDate,
				String endDate) {
			try {
				String BATCH_MANAGEMENT_SERVICE_URL = "http://BATCH-MANAGEMENT-SERVICE/batch/batch-details/employees/{batchId}";
	 
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
	 
				ResponseEntity<List<Map<String, Object>>> responseEntity = restTemplate.exchange(
						BATCH_MANAGEMENT_SERVICE_URL, HttpMethod.GET, null,
						new ParameterizedTypeReference<List<Map<String, Object>>>() {
						}, batchId);
				List<Map<String, Object>> employeeDetails = responseEntity.getBody();
				long totalEmployeesForTypeOnDate = employeeDetails.size();
				//System.out.println(totalEmployeesForTypeOnDate);
	 
				List<ReportExcelDto> reportData = generateAttendanceReportDetails(batchId, courseId, startDate, endDate);
	 
				Map<String, Map<String, Double>> dateAttendanceMap = new HashMap<>();
				for (ReportExcelDto report : reportData) {
					for (AttendanceDetail detail : report.getAttendance()) {
						if (!detail.getType().equalsIgnoreCase("Not available")) {
							String date1 = detail.getDate();
							String type = detail.getType();
	 
							// Check if the attendance is full day, then split into first and second half
							if (detail.getStatus().equalsIgnoreCase("Present")
									&& detail.getType().equalsIgnoreCase("Full Day")) {
								long presentEmployeesForFullDay = reportData.stream()
										.filter(r -> r.getAttendance().stream()
												.anyMatch(d -> d.getDate().equals(date1)
														&& d.getType().equalsIgnoreCase("Full Day")
														&& d.getStatus().equalsIgnoreCase("Present")))
										.count();
	 
								// Calculate the attendance percentage for full day
								double attendancePercentageForFullDay = (presentEmployeesForFullDay * 100.0)
										/ totalEmployeesForTypeOnDate;
	 
								// Update the attendance percentage for the date and types
								dateAttendanceMap.computeIfAbsent(date1, k -> new HashMap<>());
								dateAttendanceMap.get(date1).put("First Half", attendancePercentageForFullDay);
								dateAttendanceMap.get(date1).put("Second Half", attendancePercentageForFullDay);
							} else {
								long presentEmployeesForTypeOnDate = reportData.stream()
										.filter(r -> r.getAttendance().stream().anyMatch(
												d -> d.getDate().equals(date1) && d.getType().equalsIgnoreCase(type)
														&& d.getStatus().equalsIgnoreCase("Present")))
										.count();
								System.out.println(presentEmployeesForTypeOnDate);
	 
								double attendancePercentageForTypeOnDate = (presentEmployeesForTypeOnDate * 100.0)
										/ totalEmployeesForTypeOnDate;
	 
								// Update the attendance percentage for the date and type
								dateAttendanceMap.computeIfAbsent(date1, k -> new HashMap<>());
								dateAttendanceMap.get(date1).put(type, attendancePercentageForTypeOnDate);
							}
						}
					}
				}
	 
				// Ensure "First Half" and "Second Half" are present for each date
				for (String dateKey : dateAttendanceMap.keySet()) {
					Map<String, Double> typeMap = dateAttendanceMap.get(dateKey);
					if (!typeMap.containsKey("First Half")) {
						typeMap.put("First Half", -1.0); // Indicate "Not Taken" with -1.0
					}
					if (!typeMap.containsKey("Second Half")) {
						typeMap.put("Second Half", -1.0); // Indicate "Not Taken" with -1.0
					}
					typeMap.remove("Full Day"); // Remove "Full Day" entries if present
				}
	 
				return dateAttendanceMap;
			} catch (HttpClientErrorException.NotFound e) { 
				if (e.getMessage().contains("batch")) {
					throw new BatchNotFoundException("Batch not found with ID: " + batchId);
				} else if (e.getMessage().contains("course")) {
					throw new CourseNotFoundException("Course not found with ID: " + courseId);
				}
				throw new AttendanceCalculationException("Attendance calculation failed: " + e.getMessage());
			} catch (Exception e) {
				throw new AttendanceCalculationException("Attendance calculation failed: " + e.getMessage());
			}
	}
	 
	
	// list of all batches and courses on a specific date
    public List<Long> findBatchIdsWithAttendanceOnDate(String date) {
        List<Attendance> attendances = attendanceRepository.findByAttendance_Date(date);
        return attendances.stream()
                .map(Attendance::getBatchId)
                .collect(Collectors.toList());
    }

    public Set<Long> findCoursesOnDateForBatch(Long batchId, String date) {
        List<Attendance> attendances = attendanceRepository.findByBatchIdAndAttendance_Date(batchId, date);
        return attendances.stream()
                .map(Attendance::getCourseId)
                //.map(Object::toLong) // Assuming courseId is Long, convert to String
                .collect(Collectors.toSet()); // Use Collectors.toSet() to collect unique course IDs
    }

    public Map<String, Object> calculateAttendancePercentageWithHalfDays(Long batchId, Long courseId, String date) {
        try {
            String BATCH_MANAGEMENT_SERVICE_URL = "http://BATCH-MANAGEMENT-SERVICE/batch/batch-details/employees/{batchId}";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String LEARNING_PLAN_SERVICE_URL = "http://LEARNING-PLAN-SERVICE/course/id/{courseId}";
            ResponseEntity<Map<String, Object>> responseEntity1 = restTemplate.exchange(
                    LEARNING_PLAN_SERVICE_URL, HttpMethod.GET, null,
                    new ParameterizedTypeReference<Map<String, Object>>() {}, courseId);
            Map<String, Object> courseData = responseEntity1.getBody();  
            String courseName = (String) courseData.get("courseName");
            
            String BATCH_MANAGEMENT_SERVICE_URL_NAME = "http://BATCH-MANAGEMENT-SERVICE/batch/batch-name/{batchId}";
            ResponseEntity<Map<String, Object>> responseEntity2 = restTemplate.exchange(
            		BATCH_MANAGEMENT_SERVICE_URL_NAME, HttpMethod.GET, null,
                    new ParameterizedTypeReference<Map<String, Object>>() {}, batchId);
            Map<String, Object> batchData = responseEntity2.getBody();  
            String batchName = (String) batchData.get("batchName");
            
            ResponseEntity<List<Map<String, Object>>> responseEntity = restTemplate.exchange(
                    BATCH_MANAGEMENT_SERVICE_URL, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}, batchId);
            List<Map<String, Object>> employeeDetails = responseEntity.getBody();
            long totalEmployeesForTypeOnDate = employeeDetails.size();	

            List<ReportExcelDto> reportData = generateAttendanceReportDetails(batchId, courseId, date);

            // Calculate total present employees for the date
            long totalPresentEmployees = reportData.stream()
                    .filter(report -> report.getAttendance().stream()
                            .anyMatch(detail -> detail.getDate().equals(date) && detail.getStatus().equalsIgnoreCase("Present")))
                    .count();

            // Calculate overall attendance percentage for the date
            long overallAttendancePercentage = (totalPresentEmployees * 100) / totalEmployeesForTypeOnDate;
            // Return a map with both the overall attendance percentage and course name
            
            Map<String, Object> attendanceMap = new HashMap<>();
            attendanceMap.put("percentage", overallAttendancePercentage);
            attendanceMap.put("courseName", courseName);
            attendanceMap.put("batchName", batchName);
            return attendanceMap;
        } catch (HttpClientErrorException.NotFound e) {
            if (e.getMessage().contains("batch")) {
                throw new BatchNotFoundException("Batch not found with ID: " + batchId);
            } else if (e.getMessage().contains("course")) {
                throw new CourseNotFoundException("Course not found with ID: " + courseId);
            }
            throw new AttendanceCalculationException("Attendance calculation failed: " + e.getMessage());
        } catch (Exception e) {
            throw new AttendanceCalculationException("Attendance calculation failed: " + e.getMessage());
        }
    }



}