package com.thbs.attendance.Controller;

import com.thbs.attendance.DTO.AttendanceUpdateDTO;
import com.thbs.attendance.Entity.Attendance;
import com.thbs.attendance.Service.AttendanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AttendanceControllerTest {

    @InjectMocks
    private AttendanceController attendanceController;

    @Mock
    private AttendanceService attendanceService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testUpdateAttendance() {
        // Create mock objects
        AttendanceUpdateDTO attendanceUpdateDTO = new AttendanceUpdateDTO();
        Attendance attendance = new Attendance();

        // Mock behavior
        when(attendanceService.processAttendanceUpdate(attendanceUpdateDTO)).thenReturn(attendance);

        // Call the controller method
        ResponseEntity<Attendance> responseEntity = attendanceController.updateAttendance(attendanceUpdateDTO);

        // Verify that the service method was called
        verify(attendanceService, times(1)).processAttendanceUpdate(attendanceUpdateDTO);

        // Check the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(attendance, responseEntity.getBody());
    }
}