package org.motechproject.mtraining.service;

import org.motechproject.mtraining.domain.Chapter;
import org.motechproject.mtraining.domain.Course;
import org.motechproject.mtraining.domain.Lesson;
import org.motechproject.mtraining.domain.Quiz;

import java.util.List;

/**
 * Service interface for mTraining. Contains APIs to perform CRUD operations on Course and related objects like
 * chapters, lessons, quiz, etc
 */
public interface MTrainingService {

    /**
     *  Course CRUD
     */

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
     * @param courseId ID of the course to delete
     */
    void deleteCourse(long courseId);

    /**
     * Chapter CRUD
     */

    /**
     * Create a chapter
     * @param chapter chapter to create
     * @return chapter created in the store
     */
    Chapter createChapter(Chapter chapter);

    /**
     * Get chapter from store by ID
     * @param chapterId the chapter ID
     * @return Chapter with id
     */
    Chapter getChapterById(long chapterId);

    /**
     *  Get chapter by name
     * @param chapterName name of the chapter
     * @return list of chapters with matching name
     */
    List<Chapter> getChapterByName(String chapterName);

    /**
     * Update a given chapter
     * @param chapter chapter to update
     * @return updated chapter from the store
     */
    Chapter updateChapter(Chapter chapter);

    /**
     * Delete a chapter by id
     * @param chapterId id of the chapter to delete
     */
    void deleteChapter(long chapterId);

    /**
     * Lesson CRUD
     */

    /**
     * Create a lesson
     * @param lesson lesson to create in store
     * @return stored lesson from store
     */
    Lesson createLesson(Lesson lesson);

    /**
     * get lesson by name
     * @param lessonName name of the lesson
     * @return list of lessons with matching name
     */
    List<Lesson> getLessonByName(String lessonName);

    /**
     * get lesson by id
     * @param id id of the lesson
     * @return lesson with id
     */
    Lesson getLessonById(long id);

    /**
     * update a lesson in the store
     * @param lesson lesson to update
     * @return updated lesson from store
     */
    Lesson updateLesson(Lesson lesson);

    /**
     * delete a lesson with a given id
     * @param lessonId id of the lesson to delete
     */
    void deleteLesson(long lessonId);

    /**
     * Quiz CRUD
     */

    /**
     * Create a quiz
     * @param quiz New quiz object to store
     * @return quiz object from store
     */
    Quiz createQuiz(Quiz quiz);

    /**
     * Get a quiz by a name
     * @param quizName name of the quiz
     * @return list of quiz objects with the given name
     */
    List<Quiz> getQuizByName(String quizName);

    /**
     * Get a quiz by id
     * @param id Id of the quiz
     * @return quiz object with id
     */
    Quiz getQuizById(long id);

    /**
     * update a quiz object in store
     * @param quiz quiz object to update
     * @return updated quiz object from store
     */
    Quiz updateQuiz(Quiz quiz);

    /**
     * delete a quiz with id
     * @param quizId id of the quiz to delete
     */
    void deleteQuiz(long quizId);

    /**
     * get the quiz for a given chapter
     * @param chapterId chapter id to retrieve quiz for
     * @return Quiz object for the chapter
     */
    Quiz getQuizForChapter(long chapterId);
}
