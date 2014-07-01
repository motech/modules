package org.motechproject.mtraining.service.impl;

import org.motechproject.mtraining.domain.Chapter;
import org.motechproject.mtraining.domain.Course;
import org.motechproject.mtraining.domain.Quiz;
import org.motechproject.mtraining.repository.CourseDataService;
import org.motechproject.mtraining.service.MTrainingService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Service implementation for mTraining
 */
public class MTrainingServiceImpl implements MTrainingService {

    @Autowired
    private CourseDataService courseDataService;
    /**
     * Create a course with the given structure
     * @param course course object to store
     * @return Course object created in the store
     */
    public Course createCourse(Course course) {

        return courseDataService.create(course);
    }

    /**
     * Retrieve a course with the given course id
     * @param courseId id of the course to retrieve
     * @return course with id
     */
    public Course getCourseById(long courseId) {
        return courseDataService.findCourseById(courseId);
    }

    /**
     * Get courses that match the name
     * @param courseName name of the course
     * @return list of courses that match the course name
     */
    public List<Course> getCourseByName(String courseName) {
        return courseDataService.findCourseByName(courseName);
    }

    /**
     * Update a course with the given structure
     * @param course Course structure to update
     * @return updated version of the course
     */
    public Course updateCourse(Course course) {
        // TODO : figure out versioning in Seuss
        return courseDataService.update(course);
    }

    /**
     * Delete the course with the given id
     * @param courseId id of the course
     */
    public void deleteCourse(long courseId) {
        Course toDelete = getCourseById(courseId);
        courseDataService.delete(toDelete);
    }

    /**
     * Change the status of a course to active/inactive. status = true = active
     * @param courseId id of the course
     * @param status status to set for the course
     */
    public void toggleCourseStatus(long courseId, boolean status) {
        Course toUpdate = getCourseById(courseId);
        toUpdate.setStatus(status);
        courseDataService.update(toUpdate);
    }

    /**
     * get the quiz for a given chapter
     * @param chapterId chapter id to retrieve quiz for
     * @return Quiz object for the chapter
     */
    public Quiz getQuizForChapter(long chapterId) {
        Chapter lookup = courseDataService.findChapterById(chapterId);
        return lookup.getQuiz();
    }
}
