package org.motechproject.mtraining.service.ut;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.mtraining.domain.*;
import org.motechproject.mtraining.repository.ChapterDataService;
import org.motechproject.mtraining.repository.CourseDataService;
import org.motechproject.mtraining.repository.LessonDataService;
import org.motechproject.mtraining.repository.QuizDataService;
import org.motechproject.mtraining.service.MTrainingService;
import org.motechproject.mtraining.service.impl.MTrainingServiceImpl;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Unit tests for mTraining service
 */
public class MTrainingServiceUnitTest {

    @Mock
    private MTrainingService mTrainingService;

    @Mock
    private CourseDataService courseDataService;

    @Mock
    private ChapterDataService chapterDataService;

    @Mock
    private LessonDataService lessonDataService;

    @Mock
    private QuizDataService quizDataService;

    @Before
    public void setup() {
        initMocks(this);
        mTrainingService = new MTrainingServiceImpl(courseDataService, chapterDataService, lessonDataService, quizDataService);
    }

    @Test
    public void createCourse() {
        Course newCourse = new Course("foo", CourseUnitState.Active, "bar");
        when(courseDataService.create((Course) anyObject())).thenReturn(newCourse);
        assertEquals(newCourse, mTrainingService.createCourse(newCourse));
    }

    @Test
    public void getCourseByName() {
        List<Course> courses = (List<Course>) mock(List.class);
        when(courseDataService.findCourseByName("foobar")).thenReturn(courses);
        assertEquals(courses, mTrainingService.getCourseByName("foobar"));
    }

    @Test
    public void getEmptyCourseByName() {
        assertEquals(0, mTrainingService.getCourseByName("barbaz").size());
    }

    @Test
    public void getCourseById() {
        Course newCourse = new Course("foo", CourseUnitState.Active, "bar");
        when(courseDataService.findCourseById(11)).thenReturn(newCourse);
        assertEquals(newCourse, mTrainingService.getCourseById(11));
    }

    @Test
    public void getChapterByName() {
        List<Chapter> chapters = (List<Chapter>) mock(List.class);
        when(chapterDataService.findChapterByName("foobar")).thenReturn(chapters);
        assertEquals(chapters, mTrainingService.getChapterByName("foobar"));
    }

    @Test
    public void getEmptyChapterByName() {
        assertEquals(0, mTrainingService.getChapterByName("far").size());
    }

    @Test
    public void getChapterById() {
        Chapter newChapter = new Chapter("foo", CourseUnitState.Inactive, "bar");
        when(chapterDataService.findChapterById(11)).thenReturn(newChapter);
        assertEquals(newChapter, mTrainingService.getChapterById(11));
    }

    @Test
    public void getLessonByName() {
        List<Lesson> lessons = (List<Lesson>) mock(List.class);
        when(lessonDataService.findLessonByName("foobar")).thenReturn(lessons);
        assertEquals(lessons, mTrainingService.getLessonByName("foobar"));
    }

    @Test
    public void getEmptyLessonByName() {
        assertEquals(0, mTrainingService.getLessonByName("totototo").size());
    }

    @Test
    public void getLessonById() {
        Lesson newLesson = new Lesson("foo", CourseUnitState.Inactive, "bar");
        when(lessonDataService.findLessonById(11)).thenReturn(newLesson);
        assertEquals(newLesson, mTrainingService.getLessonById(11));
    }

    @Test
    public void getQuizByName() {
        List<Quiz> quizList = (List<Quiz>) mock(List.class);
        when(quizDataService.findQuizByName("foobar")).thenReturn(quizList);
        assertEquals(quizList, mTrainingService.getQuizByName("foobar"));
    }

    @Test
    public void getEmptyQuizByName() {
        assertEquals(0, mTrainingService.getQuizByName("fooQuiz").size());
    }

    @Test
    public void getQuizById() {
        Quiz newQuiz = new Quiz("foo", CourseUnitState.Inactive, "bar");
        when(quizDataService.findQuizById(11)).thenReturn(newQuiz);
        assertEquals(newQuiz, mTrainingService.getQuizById(11));
    }

    @Test
    public void getQuizForChapter() {
        Quiz newQuiz = new Quiz("foo", CourseUnitState.Inactive, "bar");
        Chapter parentChapter = new Chapter("chapterName", CourseUnitState.Active, null, null, newQuiz);
        when(chapterDataService.findChapterById(11)).thenReturn(parentChapter);
        assertEquals(newQuiz, mTrainingService.getQuizForChapter(11));
    }
}
