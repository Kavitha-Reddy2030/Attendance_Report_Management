package com.thbs.attendance.Controller;

import com.thbs.attendance.DTO.AttendanceUpdateDTO;
import com.thbs.attendance.Entity.Attendance;
import com.thbs.attendance.Service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @PostMapping("/attendance/update")
    public ResponseEntity<Attendance> updateAttendance(@RequestBody AttendanceUpdateDTO attendanceData) {
        
        // Pass the Attendance object to the service for processing
        return ResponseEntity.ok(attendanceService.processAttendanceUpdate(attendanceData));
    }
    
}