package com.thbs.attendance.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thbs.attendance.DTO.BatchCourseDTO;
import com.thbs.attendance.DTO.BatchesDTO;
import com.thbs.attendance.DTO.EmployeeDTO;
import com.thbs.attendance.Service.BatchService;

@RestController
@RequestMapping("/attendance")
public class BatchController {

    @Autowired
    BatchService batchService;

    @GetMapping("/batch/{batchId}/date/{date}/courses")
    public ResponseEntity<BatchCourseDTO> getCourses(@PathVariable long batchId,@PathVariable String date) {
        BatchCourseDTO batchCourseDTO = batchService.getAvailableCourses(batchId,date);
        return ResponseEntity.ok(batchCourseDTO);
    }

    @GetMapping("/batches")
    public ResponseEntity<List<BatchesDTO>> getBatches() {
        List<BatchesDTO> batchesDTO = batchService.getBatches();
        return ResponseEntity.ok(batchesDTO);
    }

    @GetMapping("/batch/employees/{batchId}")
    public List<EmployeeDTO> getEmployeesByBatchId(@PathVariable long batchId) {
        return batchService.getEmployeesByBatchId(batchId);
    }

}