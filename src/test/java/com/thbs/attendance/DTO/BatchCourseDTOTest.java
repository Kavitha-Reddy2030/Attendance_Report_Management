package com.thbs.attendance.DTO;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BatchCourseDTOTest {

    @Test
    public void testBatchCourseDTOConstructor() {
        // Test data
        long batchId = 1L;
        List<Courses> courses = new ArrayList<>();

        // Create BatchCourseDTO object using constructor
        BatchCourseDTO batchCourseDTO = new BatchCourseDTO(batchId, courses);

        // Verify data
        assertEquals(batchId, batchCourseDTO.getBatchId());
        assertEquals(courses, batchCourseDTO.getCourses());
    }

    @Test
    public void testBatchCourseDTOSetterGetter() {
        // Test data
        long batchId = 1L;
        List<Courses> courses = new ArrayList<>();

        // Create BatchCourseDTO object
        BatchCourseDTO batchCourseDTO = new BatchCourseDTO();

        // Set data using setters
        batchCourseDTO.setBatchId(batchId);
        batchCourseDTO.setCourses(courses);

        // Verify data using getters
        assertEquals(batchId, batchCourseDTO.getBatchId());
        assertEquals(courses, batchCourseDTO.getCourses());
    }
}
