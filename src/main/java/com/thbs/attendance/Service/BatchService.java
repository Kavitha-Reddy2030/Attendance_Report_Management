package com.thbs.attendance.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.thbs.attendance.DTO.BatchCourseDTO;
import com.thbs.attendance.DTO.BatchesDTO;
import com.thbs.attendance.DTO.Courses;
import com.thbs.attendance.DTO.EmployeeDTO;
import com.thbs.attendance.Entity.Attendance;
import com.thbs.attendance.Entity.AttendanceDetail;
import com.thbs.attendance.Exception.BatchIdNotFoundException;
import com.thbs.attendance.Exception.UserNotFoundException;
import com.thbs.attendance.Repository.AttendanceRepository;

@Service
public class BatchService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Value("${learninPlanService.uri}")
    private String learningPlanServiceUri;

    @Value("${batchService.uri}")
    private String batchServiceUri;

    @Value("${batchEmployees.uri}")
    private String batchEmployeesUri;

    public BatchCourseDTO getCourses(long batchId) {
        String uri = UriComponentsBuilder.fromUriString(learningPlanServiceUri)
                .buildAndExpand(batchId)
                .toUriString();

        try {
            ResponseEntity<BatchCourseDTO> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<BatchCourseDTO>() {
                    });
            if (response == null || response.getBody() == null) {
                throw new BatchIdNotFoundException("Batch with ID " + batchId + " not found.");
            }

            return response.getBody();
        } catch (HttpClientErrorException.NotFound ex) {
            throw new BatchIdNotFoundException("Batch with ID " + batchId + " not found.");
        }
    }

    public List<BatchesDTO> getBatches() {
        String uri = UriComponentsBuilder.fromUriString(batchServiceUri).toUriString();
        BatchesDTO[] response = restTemplate.getForObject(uri, BatchesDTO[].class);
        return Arrays.asList(response);
    }

    public List<EmployeeDTO> getEmployeesByBatchId(long batchId) {
        String url = UriComponentsBuilder.fromUriString(batchEmployeesUri)
                .buildAndExpand(batchId)
                .toUriString();
        try {
            EmployeeDTO[] employeeArray = restTemplate.getForObject(url, EmployeeDTO[].class);
            if (employeeArray == null || employeeArray.length == 0) {
                throw new UserNotFoundException("No employees found for batch with ID " + batchId);
            }
            return Arrays.asList(employeeArray);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new BatchIdNotFoundException("Batch with ID " + batchId + " not found.");
        }
    }

    public BatchCourseDTO getAvailableCourses(Long batchId, String date) {
        List<Attendance> attendances = attendanceRepository.findByBatchId(batchId);
    
        // Check if attendances list is empty
        if (attendances.isEmpty()) {
            return getCourses(batchId);
        }
    
        boolean attendanceFoundForDate = false;
        Long availableCourseId = null;
    
        // Check for attendance on the specified date
        for (Attendance attendance : attendances) {
            for (AttendanceDetail attendanceDetail : attendance.getAttendance()) {
                if (attendanceDetail.getDate().equals(date)) {
                    attendanceFoundForDate = true;
                    availableCourseId = attendance.getCourseId();
                    break;
                }
            }
            if (attendanceFoundForDate) {
                break;
            }
        }
    
        // If no attendance found for the specified date, return all courses
        if (!attendanceFoundForDate) {
            return getCourses(batchId);
        }
    
        // If attendance found, filter and return the specific course
        BatchCourseDTO batchCourseDTO = getCourses(batchId);
        List<Courses> allCourses = batchCourseDTO.getCourses();
        List<Courses> filteredCourses = new ArrayList<>();
    
        for (Courses course : allCourses) {
            if (course.getCourseId()==(availableCourseId)) {
                filteredCourses.add(course);
                break;
            }
        }
    
        return new BatchCourseDTO(batchId, filteredCourses);
    }
    
    

}