package com.thbs.attendance.Repository;

import com.thbs.attendance.Entity.Attendance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
public class AttendanceRepositoryTest {

    @Autowired
    private AttendanceRepository attendanceRepository;

    // @Test
    // public void testFindByUserIdAndCourseIdAndBatchId() {
    //     // Test data
    //     Long userId = 1L;
    //     Long courseId = 1L;
    //     Long batchId = 1L;
    //     Attendance attendance = new Attendance();
    //     attendance.setUserId(userId);
    //     attendance.setCourseId(courseId);
    //     attendance.setBatchId(batchId);
    //     attendanceRepository.save(attendance);

    //     // Perform repository operation
    //     Attendance foundAttendance = attendanceRepository.findByUserIdAndCourseIdAndBatchId(userId, batchId, courseId);

    //     // Verify result
    //     assertNotNull(foundAttendance);
    //     assertEquals(userId, foundAttendance.getUserId());
    //     assertEquals(courseId, foundAttendance.getCourseId());
    //     assertEquals(batchId, foundAttendance.getBatchId());
    // }

    @Test
    public void testFindByBatchIdAndCourseId() {
        // Test data
        Long batchId = 1L;
        Long courseId = 1L;
        Attendance attendance1 = new Attendance();
        attendance1.setBatchId(batchId);
        attendance1.setCourseId(courseId);
        attendanceRepository.save(attendance1);

        // Perform repository operation
        List<Attendance> attendanceList = attendanceRepository.findByBatchIdAndCourseId(batchId, courseId);

        // Verify result
        assertNotNull(attendanceList);
        assertEquals(batchId, attendanceList.get(0).getBatchId());
        assertEquals(courseId, attendanceList.get(0).getCourseId());
    }
}