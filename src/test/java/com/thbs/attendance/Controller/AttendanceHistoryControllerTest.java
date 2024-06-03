package com.thbs.attendance.Controller;

import com.thbs.attendance.DTO.EmployeeAttendanceDTO;
import com.thbs.attendance.Service.AttendanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AttendanceHistoryControllerTest {

    @InjectMocks
    private AttendanceHistoryController attendanceHistoryController;

    @Mock
    private AttendanceService attendanceService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testGetAvailableSlots() {
        // Mock data
        Long batchID = 1L;
        Long courseId = 2L;
        String date = "2024-05-11";
        List<String> availableSlots = new ArrayList<>();
        availableSlots.add("slot1");
        availableSlots.add("slot2");

        // Mock behavior
        when(attendanceService.getAvailableSlots(batchID, courseId, date)).thenReturn(availableSlots);

        // Call the controller method
        ResponseEntity<List<String>> responseEntity = attendanceHistoryController.getAvailableSlots(batchID, courseId, date);

        // Verify that the service method was called
        verify(attendanceService, times(1)).getAvailableSlots(batchID, courseId, date);

        // Check the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(availableSlots, responseEntity.getBody());
    }
}