package org.motechproject.mtraining.service;

import org.motechproject.mtraining.domain.Chapter;
import org.motechproject.mtraining.domain.Course;
import org.motechproject.mtraining.domain.Lesson;
import org.motechproject.mtraining.domain.Quiz;

import java.util.List;
import java.util.Map;

/**
 * Service interface for mTraining. Contains APIs to perform CRUD operations on Course and related objects like
 * chapters, lessons, quiz, etc.
 */
public interface MTrainingService {

    /**
     *  Course CRUD
     */

    /**
     * Creates a course with the given structure.
     *
     * @param course course object to store
     * @return Course object created in the store
     */
    Course createCourse(Course course);

    /**
     * Retrieves a course with the given course id.
     *
     * @param courseId id of the course to retrieve
     * @return the course with the given id, or null if it does not exist
     */
    Course getCourseById(long courseId);

    /**
     * Gets courses that match the name.
     *
     * @param courseName name of the course
     * @return list of courses that match the course name
     */
    List<Course> getCoursesByName(String courseName);

    /**
     * Gets courses that contains the given properties.
     *
     * @param properties the properties
     * @return list of courses that contains the given properties
     */
    List<Course> getCoursesByProperties(Map<String, String> properties);

    /**
     * Gets all courses.
     *
     * @return list of courses
     */
    List<Course> getAllCourses();

    /**
     * Updates a course with the given structure
     * @param course Course structure to update
     * @return the newly updated course
     */
    Course updateCourse(Course course);

    /**
     * Deletes the course with the given id
     * @param courseId ID of the course to delete
     */
    void deleteCourse(long courseId);

    /**
     * Chapter CRUD
     */

    /**
     * Creates a chapter.
     *
     * @param chapter chapter to create
     * @return chapter created in the store
     */
    Chapter createChapter(Chapter chapter);

    /**
     * Gets chapters from store by ID.
     *
     * @param chapterId the chapter ID
     * @return Chapter with id
     */
    Chapter getChapterById(long chapterId);

    /**
     * Gets chapters that match the name.
     *
     * @param chapterName name of the chapter
     * @return list of chapters with matching name
     */
    List<Chapter> getChaptersByName(String chapterName);

    /**
     * Gets chapters that contains the given properties.
     *
     * @param properties the properties
     * @return list of chapters that contains the given properties
     */
    List<Chapter> getChaptersByProperties(Map<String, String> properties);

    /**
     * Get all chapters.
     *
     * @return list of chapters
     */
    List<Chapter> getAllChapters();

    /**
     * Updates a given chapter.
     *
     * @param chapter chapter to update
     * @return updated chapter from the store
     */
    Chapter updateChapter(Chapter chapter);

    /**
     * Deletes a chapter by id.
     *
     * @param chapterId id of the chapter to delete
     */
    void deleteChapter(long chapterId);

    /**
     * Lesson CRUD
     */

    /**
     * Creates a lesson.
     *
     * @param lesson lesson to create in store
     * @return stored lesson from store
     */
    Lesson createLesson(Lesson lesson);

    /**
     * Gets lessons that match the name.
     *
     * @param lessonName name of the lesson
     * @return list of lessons with matching name
     */
    List<Lesson> getLessonsByName(String lessonName);

    /**
     * Gets lessons that contains the given properties.
     *
     * @param properties the properties
     * @return list of lessons that contains the given properties
     */
    List<Lesson> getLessonsByProperties(Map<String, String> properties);

    /**
     * Gets lesson by id.
     *
     * @param id id of the lesson
     * @return lesson with id
     */
    Lesson getLessonById(long id);

    /**
     * Gets all lessons.
     *
     * @return list of lessons
     */
    List<Lesson> getAllLessons();

    /**
     * Updates a lesson in the store.
     *
     * @param lesson lesson to update
     * @return updated lesson from store
     */
    Lesson updateLesson(Lesson lesson);

    /**
     * Deletes a lesson with a given id.
     *
     * @param lessonId id of the lesson to delete
     */
    void deleteLesson(long lessonId);

    /**
     * Quiz CRUD
     */

    /**
     * Creates a quiz.
     *
     * @param quiz New quiz object to store
     * @return quiz object from store
     */
    Quiz createQuiz(Quiz quiz);

    /**
     * Gets a quizzes that match the name.
     *
     * @param quizName name of the quiz
     * @return list of quiz objects with the given name
     */
    List<Quiz> getQuizzesByName(String quizName);

    /**
     * Gets quizzes that contains the given properties.
     *
     * @param properties the properties
     * @return list of quizzes that contains the given properties
     */
    List<Quiz> getQuizzesByProperties(Map<String, String> properties);

    /**
     * Gets a quiz by id.
     *
     * @param id Id of the quiz
     * @return quiz object with id
     */
    Quiz getQuizById(long id);

    /**
     * Gets all quizzes.
     *
     * @return list of quizzes
     */
    List<Quiz> getAllQuizzes();

    /**
     * Updates a quiz object in store.
     *
     * @param quiz quiz object to update
     * @return updated quiz object from store
     */
    Quiz updateQuiz(Quiz quiz);

    /**
     * Deletes a quiz with id.
     *
     * @param quizId id of the quiz to delete
     */
    void deleteQuiz(long quizId);

    /**
     * Gets the quiz for a given chapter.
     *
     * @param chapterId chapter id to retrieve quiz for
     * @return Quiz object for the chapter
     */
    Quiz getQuizForChapter(long chapterId);
}
