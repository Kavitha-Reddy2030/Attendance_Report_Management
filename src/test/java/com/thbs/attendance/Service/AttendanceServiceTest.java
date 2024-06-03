package com.thbs.attendance.Service;

import com.thbs.attendance.DTO.AttendanceDetailDTO;
import com.thbs.attendance.DTO.AttendanceUpdateDTO;
import com.thbs.attendance.DTO.EmployeeAttendanceDTO;
import com.thbs.attendance.DTO.EmployeeDTO;
import com.thbs.attendance.Entity.Attendance;
import com.thbs.attendance.Entity.AttendanceDetail;
import com.thbs.attendance.Repository.AttendanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AttendanceServiceTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private BatchService batchService;

    @InjectMocks
    private AttendanceService attendanceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessAttendanceUpdate() {
        // Test data
        AttendanceUpdateDTO attendanceData = new AttendanceUpdateDTO();
        attendanceData.setBatchId(1L);
        attendanceData.setCourseId(1L);
        attendanceData.setDate("2024-05-12");
        attendanceData.setType("Full Day");
        AttendanceDetailDTO detailDTO = new AttendanceDetailDTO();
        detailDTO.setUserId(1L);
        detailDTO.setStatus("Present");
        attendanceData.setAttendance(Collections.singletonList(detailDTO));
        Attendance attendance = new Attendance(1L, 1L, 1L, Collections.emptyList());
        
        // Mock behavior
        when(attendanceRepository.findByUserIdAndCourseIdAndBatchId(anyLong(), anyLong(), anyLong())).thenReturn(null);
        when(attendanceRepository.save(any(Attendance.class))).thenReturn(attendance);
        
        // Perform service method
        Attendance result = attendanceService.processAttendanceUpdate(attendanceData);
        
        // Verify result
        verify(attendanceRepository, times(1)).findByUserIdAndCourseIdAndBatchId(anyLong(), anyLong(), anyLong());
        verify(attendanceRepository, times(1)).save(any(Attendance.class));
    }

    @Test
    void testUpdateExistingAttendanceDetail() {
        // Test data
        Attendance attendance = new Attendance();
        AttendanceDetail detail1 = new AttendanceDetail("2024-05-12", "Full Day", "Present");
        attendance.setAttendance(Collections.singletonList(detail1));
        when(attendanceRepository.save(any(Attendance.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Call method
        attendanceService.updateExistingAttendanceDetail(attendance, "2024-05-12", "Full Day", "Absent");

        // Verify result
        assertEquals("Absent", attendance.getAttendance().get(0).getStatus());
        verify(attendanceRepository, times(1)).save(any(Attendance.class));
    }

    @Test
    void testCreateNewAttendanceRecord() {
        // Test data
        when(attendanceRepository.save(any(Attendance.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Call method
        attendanceService.createNewAttendanceRecord(1L, 1L, 1L, "2024-05-12", "Full Day", "Present");

        // Verify result
        verify(attendanceRepository, times(1)).save(any(Attendance.class));
    }

    @Test
    void testGetAttendanceDetails() {
        // Test data
        List<EmployeeDTO> employees = Collections.singletonList(new EmployeeDTO(1L, "John", "Doe", "john@example.com", "Role", "BusinessUnit"));
        List<Attendance> attendances = Collections.singletonList(new Attendance(1L, 1L, 1L, Collections.singletonList(new AttendanceDetail("2024-05-12", "Full Day", "Present"))));
        when(batchService.getEmployeesByBatchId(anyLong())).thenReturn(employees);
        when(attendanceRepository.findByBatchIdAndCourseId(anyLong(), anyLong())).thenReturn(attendances);

        // Call method
        List<EmployeeAttendanceDTO> result = attendanceService.getAttendanceDetails(1L, 1L, "2024-05-12", "Full Day");

        // Verify result
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getEmployeeId());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Doe", result.get(0).getLastName());
        assertEquals("john@example.com", result.get(0).getEmail());
        assertEquals("Role", result.get(0).getRole());
        assertEquals("BusinessUnit", result.get(0).getBusinessUnit());
        assertEquals("Present", result.get(0).getStatus());
        verify(batchService, times(1)).getEmployeesByBatchId(anyLong());
        verify(attendanceRepository, times(1)).findByBatchIdAndCourseId(anyLong(), anyLong());
    }


}
