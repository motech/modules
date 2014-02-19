package org.motechproject.mtraining.service;
import org.motechproject.mtraining.domain.Course;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation class for @CourseService
 */

@Service("courseService")
public class CourseServiceImpl implements CourseService {

    //Work in progress will add db support later
    private List<Course> allCourses;

    public CourseServiceImpl() {
        allCourses = new ArrayList<>();
    }

    @Override
    public void addCourse(Course course) {
        allCourses.add(course);
    }

    @Override
    public List<Course> getAllCourses() {
        return allCourses;
    }
}
