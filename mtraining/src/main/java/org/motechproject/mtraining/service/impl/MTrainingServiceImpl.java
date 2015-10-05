package org.motechproject.mtraining.service.impl;

import org.apache.commons.lang.StringUtils;
import org.motechproject.mds.query.QueryExecution;
import org.motechproject.mds.query.QueryExecutor;
import org.motechproject.mds.util.InstanceSecurityRestriction;
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
import org.springframework.transaction.annotation.Transactional;

import javax.jdo.Query;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Service implementation for mTraining. This lets the implementer perform crud operations for
 * course, chapter, lesson and quiz objects. This is the primary service interface implementation that the admins
 * would use to author the structure of the course.
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

    @Override
    @Transactional
    public Course createCourse(Course course) {
        return courseDataService.create(course);
    }

    @Override
    @Transactional
    public Course getCourseById(long courseId) {
        return courseDataService.findCourseById(courseId);
    }

    @Override
    @Transactional
    public List<Course> getCoursesByName(String courseName) {
        return courseDataService.findCoursesByName(courseName);
    }

    @Override
    @Transactional
    public List<Course> getCoursesByProperties(final Map<String, String> properties) {
        List courses = courseDataService.executeQuery(new QueryExecution<List<Course>>() {
            @Override
            public List<Course> execute(Query query, InstanceSecurityRestriction restriction) {
                query.setFilter(buildParametersFilter(properties));
                return (List) QueryExecutor.execute(query, restriction);
            }
        });

        return new ArrayList<>(courses);
    }

    @Override
    @Transactional
    public List<Course> getAllCourses() {
        return courseDataService.retrieveAll();
    }

    @Override
    @Transactional
    public Course updateCourse(Course course) {
        return courseDataService.update(course);
    }

    @Override
    @Transactional
    public void deleteCourse(long courseId) {
        Course toDelete = getCourseById(courseId);
        courseDataService.delete(toDelete);
    }

    @Override
    @Transactional
    public Quiz getQuizForChapter(long chapterId) {
        Chapter lookup = chapterDataService.findChapterById(chapterId);
        return lookup.getQuiz();
    }

    /**
     * Chapter CRUD
     */

    @Override
    @Transactional
    public Chapter createChapter(Chapter chapter) {
        return chapterDataService.create(chapter);
    }

    @Override
    @Transactional
    public List<Chapter> getChaptersByName(String chapterName) {
        return chapterDataService.findChaptersByName(chapterName);
    }

    @Override
    public List<Chapter> getChaptersByProperties(Map<String, String> properties) {
        List chapters = chapterDataService.executeQuery(new QueryExecution<List<Chapter>>() {
            @Override
            public List<Chapter> execute(Query query, InstanceSecurityRestriction restriction) {
                query.setFilter(buildParametersFilter(properties));
                return (List) QueryExecutor.execute(query, restriction);
            }
        });

        return new ArrayList<>(chapters);
    }

    @Override
    @Transactional
    public Chapter getChapterById(long chapterId) {
        return chapterDataService.findChapterById(chapterId);
    }

    @Override
    @Transactional
    public List<Chapter> getAllChapters() {
        return chapterDataService.retrieveAll();
    }

    @Override
    @Transactional
    public Chapter updateChapter(Chapter chapter) {
        return chapterDataService.update(chapter);
    }

    @Override
    @Transactional
    public void deleteChapter(long chapterId) {
        Chapter toDelete = chapterDataService.findChapterById(chapterId);
        chapterDataService.delete(toDelete);
    }

    /**
     * Lesson CRUD
     */

    @Override
    @Transactional
    public Lesson createLesson(Lesson lesson) {
        return lessonDataService.create(lesson);
    }

    @Override
    @Transactional
    public List<Lesson> getLessonsByName(String lessonName) {
        return lessonDataService.findLessonsByName(lessonName);
    }

    @Override
    public List<Lesson> getLessonsByProperties(Map<String, String> properties) {
        List lessons = lessonDataService.executeQuery(new QueryExecution<List<Lesson>>() {
            @Override
            public List<Lesson> execute(Query query, InstanceSecurityRestriction restriction) {
                query.setFilter(buildParametersFilter(properties));
                return (List) QueryExecutor.execute(query, restriction);
            }
        });

        return new ArrayList<>(lessons);
    }

    @Override
    @Transactional
    public Lesson getLessonById(long lessonId) {
        return lessonDataService.findLessonById(lessonId);
    }

    @Override
    public List<Lesson> getAllLessons() {
        return lessonDataService.retrieveAll();
    }

    @Override
    @Transactional
    public Lesson updateLesson(Lesson lesson) {
        return lessonDataService.update(lesson);
    }

    @Override
    @Transactional
    public void deleteLesson(long lessonId) {
        Lesson toDelete = lessonDataService.findLessonById(lessonId);
        lessonDataService.delete(toDelete);
    }

    /**
     * Quiz CRUD
     */

    @Override
    @Transactional
    public Quiz createQuiz(Quiz quiz) {
        return quizDataService.create(quiz);
    }

    @Override
    @Transactional
    public List<Quiz> getQuizzesByName(String quizName) {
        return quizDataService.findQuizzesByName(quizName);
    }

    @Override
    public List<Quiz> getQuizzesByProperties(Map<String, String> properties) {
        List quizzes = quizDataService.executeQuery(new QueryExecution<List<Quiz>>() {
            @Override
            public List<Quiz> execute(Query query, InstanceSecurityRestriction restriction) {
                query.setFilter(buildParametersFilter(properties));
                return (List) QueryExecutor.execute(query, restriction);
            }
        });

        return new ArrayList<>(quizzes);
    }

    @Override
    @Transactional
    public Quiz getQuizById(long id) {
        return quizDataService.findQuizById(id);
    }

    @Override
    @Transactional
    public List<Quiz> getAllQuizzes() {
        return quizDataService.retrieveAll();
    }

    @Override
    @Transactional
    public Quiz updateQuiz(Quiz quiz) {
        return quizDataService.update(quiz);
    }

    @Override
    @Transactional
    public void deleteQuiz(long quizId) {
        Quiz toDelete = quizDataService.findQuizById(quizId);
        quizDataService.delete(toDelete);
    }

    private String buildParametersFilter(Map<String, String> properties) {
        if (properties == null) {
            return "";
        }
        Collection<String> strings = new ArrayList<>();
        for(String key: properties.keySet()) {
            strings.add(String.format("properties.containsKey('%s') && properties.get('%s') == '%s'", key, key, properties.get(key)));
        }

        return String.format("%s", StringUtils.join(strings, " && "));
    }
}
