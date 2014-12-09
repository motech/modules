package org.motechproject.mtraining.service.impl;

import org.motechproject.mtraining.domain.Chapter;
import org.motechproject.mtraining.domain.Course;
import org.motechproject.mtraining.domain.Lesson;
import org.motechproject.mtraining.domain.Quiz;
import org.motechproject.mtraining.repository.ChapterDataService;
import org.motechproject.mtraining.repository.CourseDataService;
import org.motechproject.mtraining.repository.LessonDataService;
import org.motechproject.mtraining.repository.QuizDataService;
import org.motechproject.mtraining.service.MTrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for mTraining. This lets the implementer perform crud operations for
 * course, chapter, lesson and quiz objects. This is the primary service interface implementation that the admins
 * would use to author the structure of the course
 */
@Service("mTrainingService")
public class MTrainingServiceImpl implements MTrainingService {

    private CourseDataService courseDataService;

    private ChapterDataService chapterDataService;

    private LessonDataService lessonDataService;

    private QuizDataService quizDataService;

    @Autowired
    public MTrainingServiceImpl(CourseDataService courseDataService, ChapterDataService chapterDataService,
                                LessonDataService lessonDataService, QuizDataService quizDataService) {
        this.courseDataService = courseDataService;
        this.chapterDataService = chapterDataService;
        this.lessonDataService = lessonDataService;
        this.quizDataService = quizDataService;
    }
    /**
     * Create a course with the given structure
     * @param course course object to store
     * @return Course object created in the store
     */
    @Override
    public Course createCourse(Course course) {

        return courseDataService.create(course);
    }

    /**
     * Retrieve a course with the given course id
     * @param courseId id of the course to retrieve
     * @return course with id
     */
    @Override
    public Course getCourseById(long courseId) {

        return courseDataService.findCourseById(courseId);
    }

    /**
     * Get courses that match the name
     * @param courseName name of the course
     * @return list of courses that match the course name
     */
    @Override
    public List<Course> getCourseByName(String courseName) {

        return courseDataService.findCourseByName(courseName);
    }

    /**
     * Get all courses
     * @return list of courses
     */
    @Override
    public List<Course> getAllCourses() {
        return courseDataService.retrieveAll();
    }

    /**
     * Update a course with the given structure
     * @param course Course structure to update
     * @return updated version of the course
     */
    @Override
    public Course updateCourse(Course course) {

        return courseDataService.update(course);
    }

    /**
     * Delete the course with the given id
     * @param courseId id of the course
     */
    @Override
    public void deleteCourse(long courseId) {
        Course toDelete = getCourseById(courseId);
        courseDataService.delete(toDelete);
    }

    /**
     * get the quiz for a given chapter
     * @param chapterId chapter id to retrieve quiz for
     * @return Quiz object for the chapter
     */
    @Override
    public Quiz getQuizForChapter(long chapterId) {
        Chapter lookup = chapterDataService.findChapterById(chapterId);
        return lookup.getQuiz();
    }

    /**
     * Chapter CRUD
     */

    /**
     * Create a chapter with the given information
     * @param chapter chapter to create
     * @return chapter object created in the store
     */
    @Override
    public Chapter createChapter(Chapter chapter) {

        return chapterDataService.create(chapter);
    }

    /**
     * Retrieve the chapter by the given name
     * @param chapterName name of the chapter
     * @return list of chapters that match a chapter name
     */
    @Override
    public List<Chapter> getChapterByName(String chapterName) {

        return chapterDataService.findChapterByName(chapterName);
    }

    @Override
    public Chapter getChapterById(long chapterId) {

        return chapterDataService.findChapterById(chapterId);
    }

    @Override
    public List<Chapter> getAllChapters() {
        return chapterDataService.retrieveAll();
    }

    @Override
    public Chapter updateChapter(Chapter chapter) {

        return chapterDataService.update(chapter);
    }

    @Override
    public void deleteChapter(long chapterId) {
        Chapter toDelete = chapterDataService.findChapterById(chapterId);
        chapterDataService.delete(toDelete);
    }

    /**
     * Lesson CRUD
     */

    @Override
    public Lesson createLesson(Lesson lesson) {

        return lessonDataService.create(lesson);
    }

    @Override
    public List<Lesson> getLessonByName(String lessonName) {

        return lessonDataService.findLessonByName(lessonName);
    }

    @Override
    public Lesson getLessonById(long lessonId) {

        return lessonDataService.findLessonById(lessonId);
    }

    @Override
    public List<Lesson> getAllLessons() {
        return lessonDataService.retrieveAll();
    }

    @Override
    public Lesson updateLesson(Lesson lesson) {

        return lessonDataService.update(lesson);
    }

    @Override
    public void deleteLesson(long lessonId) {

        Lesson toDelete = lessonDataService.findLessonById(lessonId);
        lessonDataService.delete(toDelete);
    }

    /**
     * Quiz CRUD
     */

    @Override
    public Quiz createQuiz(Quiz quiz) {

        return quizDataService.create(quiz);
    }

    @Override
    public List<Quiz> getQuizByName(String quizName) {

        return quizDataService.findQuizByName(quizName);
    }

    @Override
    public Quiz getQuizById(long id) {

        return quizDataService.findQuizById(id);
    }

    @Override
    public List<Quiz> getAllQuizzes() {
        return quizDataService.retrieveAll();
    }

    @Override
    public Quiz updateQuiz(Quiz quiz) {

        return quizDataService.update(quiz);
    }

    @Override
    public void deleteQuiz(long quizId) {
        Quiz toDelete = quizDataService.findQuizById(quizId);
        quizDataService.delete(toDelete);
    }
}
