package org.motechproject.mtraining.service;

import org.motechproject.mtraining.domain.Course;
import org.motechproject.mtraining.domain.CoursePlan;

/**
 * Simple example of a service interface.
 */
public interface MTrainingService {

    CoursePlan createCoursePlan(CoursePlan coursePlan);

    CoursePlan getCoursePlan(String coursePlanId);

    CoursePlan updateCoursePlan(CoursePlan coursePlan);

    void deleteCoursePlan(CoursePlan coursePlan);

    Course createCourse(Course course);

    Course getCourse(String courseId);

    Course updateCourse(Course course);

    void deleteCourse(Course course);
}
