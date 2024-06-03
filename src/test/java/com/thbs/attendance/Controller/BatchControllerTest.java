package com.thbs.attendance.Controller;

import com.thbs.attendance.DTO.BatchCourseDTO;
import com.thbs.attendance.DTO.BatchesDTO;
import com.thbs.attendance.DTO.EmployeeDTO;
import com.thbs.attendance.Service.BatchService;
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

public class BatchControllerTest {

    @InjectMocks
    private BatchController batchController;

    @Mock
    private BatchService batchService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

//    @Test
//    public void testGetCourses() {
//        // Mock data
//        long batchId = 1L;
//        BatchCourseDTO batchCourseDTO = new BatchCourseDTO();
//
//        // Mock behavior
//        when(batchService.getCourses(batchId)).thenReturn(batchCourseDTO);
//
//        // Call the controller method
////        ResponseEntity<BatchCourseDTO> responseEntity = batchController.getCourses(batchId);
//
//        // Verify that the service method was called
//        verify(batchService, times(1)).getCourses(batchId);
//
//        // Check the response
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(batchCourseDTO, responseEntity.getBody());
//    }

    @Test
    public void testGetBatches() {
        // Mock data
        List<BatchesDTO> batchesDTO = new ArrayList<>();

        // Mock behavior
        when(batchService.getBatches()).thenReturn(batchesDTO);

        // Call the controller method
        ResponseEntity<List<BatchesDTO>> responseEntity = batchController.getBatches();

        // Verify that the service method was called
        verify(batchService, times(1)).getBatches();

        // Check the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(batchesDTO, responseEntity.getBody());
    }

    @Test
    public void testGetEmployeesByBatchId() {
        // Mock data
        long batchId = 1L;
        List<EmployeeDTO> employeeDTOList = new ArrayList<>();

        // Mock behavior
        when(batchService.getEmployeesByBatchId(batchId)).thenReturn(employeeDTOList);

        // Call the controller method
        List<EmployeeDTO> result = batchController.getEmployeesByBatchId(batchId);

        // Verify that the service method was called
        verify(batchService, times(1)).getEmployeesByBatchId(batchId);

        // Check the response
        assertEquals(employeeDTOList, result);
    }
}
