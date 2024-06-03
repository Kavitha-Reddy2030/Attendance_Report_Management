package com.thbs.attendance.DTO;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CoursesTest {

    @Test
    public void testCoursesConstructor() {
        // Test data
        long courseId = 1L;
        String courseName = "Math";

        // Create Courses object using constructor
        Courses courses = new Courses(courseId, courseName);

        // Verify data
        assertEquals(courseId, courses.getCourseId());
        assertEquals(courseName, courses.getCourseName());
    }

    @Test
    public void testCoursesSetterGetter() {
        // Test data
        long courseId = 1L;
        String courseName = "Math";

        // Create Courses object
        Courses courses = new Courses();

        // Set data using setters
        courses.setCourseId(courseId);
        courses.setCourseName(courseName);

        // Verify data using getters
        assertEquals(courseId, courses.getCourseId());
        assertEquals(courseName, courses.getCourseName());
    }
}
