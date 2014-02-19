package org.motechproject.mtraining.service;
import org.motechproject.mtraining.domain.Course;

import java.util.List;

/**
 * Interface that exposes APIs to manage course
 */
public interface CourseService {
    void addCourse(Course course);
    List<Course> getAllCourses();
}
