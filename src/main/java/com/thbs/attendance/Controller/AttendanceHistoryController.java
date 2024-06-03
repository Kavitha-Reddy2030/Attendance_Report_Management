package com.thbs.attendance.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import com.thbs.attendance.DTO.EmployeeAttendanceDTO;
import com.thbs.attendance.Service.AttendanceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class AttendanceHistoryController {

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("/attendance/batch/{batchID}/course/{courseId}/Date/{date}/type/{type}")
    public ResponseEntity<?> updateAttendance(@PathVariable Long batchID,
            @PathVariable Long courseId, @PathVariable String date, @PathVariable String type) {
        
        List<EmployeeAttendanceDTO> attendanceDetails = attendanceService.getAttendanceDetails(batchID, courseId, date, type);
        
        if (attendanceDetails != null && !attendanceDetails.isEmpty()) {
            return ResponseEntity.ok(attendanceDetails);
        }
        
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No attendance data found");
    }

    @GetMapping("/attendance/batch/{batchID}/course/{courseId}/Date/{date}")
    public ResponseEntity<List<String>> getAvailableSlots(@PathVariable Long batchID,@PathVariable Long courseId,@PathVariable String date) {
        return ResponseEntity.ok(attendanceService.getAvailableSlots(batchID,courseId,date));
    }
    
}