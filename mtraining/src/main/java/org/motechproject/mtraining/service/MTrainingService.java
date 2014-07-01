package org.motechproject.mtraining.service;

import org.motechproject.mtraining.domain.Course;
import org.motechproject.mtraining.domain.Quiz;

import java.util.List;

/**
 * Service interface for mTraining. Contains APIs to perform CRUD operations on Course and related object
 */
public interface MTrainingService {

    /**
     * Create a course with the given structure
     * @param course course object to store
     * @return Course object created in the store
     */
    Course createCourse(Course course);

    /**
     * Retrieve a course with the given course id
     * @param courseId id of the course to retrieve
     * @return
     */
    Course getCourseById(long courseId);

    /**
     * Get courses that match the name
     * @param courseName name of the course
     * @return list of courses that match the course name
     */
    List<Course> getCourseByName(String courseName);

    /**
     * Update a course with the given structure
     * @param course Course structure to update
     * @return
     */
    Course updateCourse(Course course);

    /**
     * Delete the course with the given id
     * @param courseId
     */
    void deleteCourse(long courseId);

    /**
     * Change the status of a course to active/inactive. status = true = active
     * @param courseId id of the course
     * @param status status to set for the course
     */
    void toggleCourseStatus(long courseId, boolean status);

    /**
     * get the quiz for a given chapter
     * @param chapterId chapter id to retrieve quiz for
     * @return Quiz object for the chapter
     */
    Quiz getQuizForChapter(long chapterId);
}
