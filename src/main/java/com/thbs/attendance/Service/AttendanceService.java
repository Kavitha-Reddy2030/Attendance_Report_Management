package com.thbs.attendance.Service;

import com.thbs.attendance.DTO.AttendanceDetailDTO;
import com.thbs.attendance.DTO.AttendanceUpdateDTO;
import com.thbs.attendance.DTO.EmployeeAttendanceDTO;
import com.thbs.attendance.DTO.EmployeeDTO;
import com.thbs.attendance.Entity.Attendance;
import com.thbs.attendance.Entity.AttendanceDetail;
import com.thbs.attendance.Exception.AttendenceRecordNotFound;
import com.thbs.attendance.Repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private BatchService batchService;

    public Attendance processAttendanceUpdate(AttendanceUpdateDTO attendanceData) {
        Long batchId = attendanceData.getBatchId();
        Long courseId = attendanceData.getCourseId();
        String date = attendanceData.getDate();
        String type = attendanceData.getType();
        List<AttendanceDetailDTO> newAttendance = attendanceData.getAttendance();

        for (AttendanceDetailDTO details : newAttendance) {
            Attendance record = attendanceRepository.findByUserIdAndCourseIdAndBatchId(details.getUserId(), courseId,
                    batchId);
            if (record != null) {
                updateExistingAttendanceDetail(record, date, type, details.getStatus());
            } else {
                createNewAttendanceRecord(batchId, courseId, details.getUserId(), date, type, details.getStatus());
            }
        }
        return null;
    }

    public void updateExistingAttendanceDetail(Attendance record, String date, String type, String status) {
        List<AttendanceDetail> attendanceList = record.getAttendance();
        for (AttendanceDetail obj : attendanceList) {
            if (obj.getDate().equals(date) && obj.getType().equals(type)) {
                obj.setStatus(status);
                attendanceRepository.save(record);
                return;
            }
        }
        AttendanceDetail newDetail = new AttendanceDetail(date, type, status);
        attendanceList.add(newDetail);
        attendanceRepository.save(record);
    }

    public void createNewAttendanceRecord(Long batchId, Long courseId, Long userId, String date, String type,
            String status) {
        List<AttendanceDetail> attendanceList = new ArrayList<>();
        attendanceList.add(new AttendanceDetail(date, type, status));
        Attendance newAttendanceRecord = new Attendance(batchId, courseId, userId, attendanceList);
        attendanceRepository.save(newAttendanceRecord);
    }

        public List<EmployeeAttendanceDTO> getAttendanceDetails(Long batchID, Long courseId, String date, String type) {
        List<EmployeeDTO> employees = batchService.getEmployeesByBatchId(batchID);
        List<Attendance> attendances = attendanceRepository.findByBatchIdAndCourseId(batchID, courseId);
        if (!attendances.isEmpty()) {
            Map<Long, String> attendanceMap = new HashMap<>();

            for (Attendance attendance : attendances) {
                for (AttendanceDetail detail : attendance.getAttendance()) {
                    if (detail.getDate().equals(date) && detail.getType().equals(type)) {
                        attendanceMap.put(attendance.getUserId(), detail.getStatus());
                    }
                }
            }
            if (!attendanceMap.isEmpty()) {
                List<EmployeeAttendanceDTO> response = new ArrayList<>();
                for (EmployeeDTO employee : employees) {
                    EmployeeAttendanceDTO employeeAttendanceDTO = new EmployeeAttendanceDTO();
                    employeeAttendanceDTO.setEmployeeId(employee.getEmployeeId());
                    employeeAttendanceDTO.setFirstName(employee.getFirstName());
                    employeeAttendanceDTO.setLastName(employee.getLastName());
                    employeeAttendanceDTO.setEmail(employee.getEmail());
                    employeeAttendanceDTO.setBusinessUnit(employee.getBusinessUnit());
                    employeeAttendanceDTO.setRole(employee.getRole());

                    String status = attendanceMap.get(employee.getEmployeeId());
                    if (status != null) {
                        employeeAttendanceDTO.setStatus(status);
                    }

                    response.add(employeeAttendanceDTO);
                }
                return response;
            } else {
                throw new AttendenceRecordNotFound("Attendence record not found.");
            }
        }
        throw new AttendenceRecordNotFound("Attendence record not found.");
    }

    public List<String> getAvailableSlots(long batchID,long courseId,String date){
        List<Attendance> attendances = attendanceRepository.findByBatchId(batchID);
        Set<String> takenSlots=new HashSet();
        List<String> availableSlots=new ArrayList<>();
        for(Attendance attendance:attendances){
            for(AttendanceDetail attendanceDetail:attendance.getAttendance()){
                if(attendanceDetail.getDate().equals(date)){
                    takenSlots.add(attendanceDetail.getType());
                }
            }
        }
        if(takenSlots.isEmpty()){
            availableSlots.addAll(Arrays.asList("First Half","Second Half","Full Day"));
        }else if(takenSlots.contains("First Half") && takenSlots.contains("Second Half")){
            return Collections.emptyList();
        }
        else if(takenSlots.contains("First Half")){
            availableSlots.add("Second Half");
        }
        else if(takenSlots.contains("Second Half")){
            availableSlots.add("First Half");
        }

        return availableSlots;
    }

}



