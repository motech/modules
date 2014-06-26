package org.motechproject.mtraining.service;

import org.motechproject.mtraining.domain.Course;
import org.motechproject.mtraining.domain.Quiz;

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
    Course getCourse(String courseId);

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
    void deleteCourse(String courseId);

    /**
     * get the quiz for a given chapter
     * @param chapterId chapter id to retrieve quiz for
     * @return Quiz object for the chapter
     */
    Quiz getQuizForChapter(String chapterId);

    /**
     * Change the status of a course to active/inactive. status = true = active
     * @param courseId id of the course
     * @param status status to set for the course
     */
    void toggleCourseStatus(String courseId, boolean status);
}
