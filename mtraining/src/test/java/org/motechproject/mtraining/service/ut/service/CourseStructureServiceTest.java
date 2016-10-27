package org.motechproject.mtraining.service.ut.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.motechproject.mtraining.domain.Chapter;
import org.motechproject.mtraining.domain.Course;
import org.motechproject.mtraining.domain.CourseUnitState;
import org.motechproject.mtraining.domain.Lesson;
import org.motechproject.mtraining.domain.Quiz;
import org.motechproject.mtraining.dto.ChapterUnitDto;
import org.motechproject.mtraining.dto.CourseUnitDto;
import org.motechproject.mtraining.dto.CourseUnitListWrapper;
import org.motechproject.mtraining.exception.CourseUnitNotFoundException;
import org.motechproject.mtraining.repository.ChapterDataService;
import org.motechproject.mtraining.repository.CourseDataService;
import org.motechproject.mtraining.repository.LessonDataService;
import org.motechproject.mtraining.repository.QuizDataService;
import org.motechproject.mtraining.service.CourseStructureService;
import org.motechproject.mtraining.service.impl.CourseStructureServiceImpl;
import org.motechproject.mtraining.util.Constants;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class CourseStructureServiceTest {

    @Mock
    private CourseDataService courseDataService;

    @Mock
    private ChapterDataService chapterDataService;

    @Mock
    private LessonDataService lessonDataService;

    @Mock
    private QuizDataService quizDataService;

    @Captor
    private ArgumentCaptor<Course> courseArgumentCaptor;

    @Captor
    private ArgumentCaptor<Chapter> chapterArgumentCaptor;

    @Captor
    private ArgumentCaptor<Lesson> lessonArgumentCaptor;

    @Captor
    private ArgumentCaptor<Quiz> quizArgumentCaptor;

    @InjectMocks
    private CourseStructureService courseStructureService = new CourseStructureServiceImpl();

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldRemoveRelations() {
        CourseUnitListWrapper coursesToUpdate = new CourseUnitListWrapper();
        coursesToUpdate.add(new CourseUnitDto(1l, Constants.COURSE, CourseUnitState.Active.toString(), null, Constants.COURSE));

        Quiz quiz = new Quiz(Constants.QUIZ, CourseUnitState.Active, Constants.QUIZ, Constants.QUIZ, null);
        quiz.setId(15l);
        Lesson lesson = new Lesson(Constants.LESSON, CourseUnitState.Active, Constants.LESSON, Constants.LESSON, null);
        lesson.setId(13l);
        List lessons = new ArrayList<>();
        lessons.add(lesson);
        Chapter chapter = new Chapter(Constants.CHAPTER , CourseUnitState.Active, Constants.CHAPTER, Constants.CHAPTER, null, lessons, quiz);
        chapter.setId(11l);
        Course course = new Course(Constants.COURSE, CourseUnitState.Active, Constants.COURSE, Constants.COURSE, null, asList(chapter));
        course.setId(1l);
        
        when(courseDataService.detachedCopy(courseDataService.findCourseById(1l))).thenReturn(course);

        courseStructureService.updateCourseStructure(coursesToUpdate);

        verify(courseDataService).update(courseArgumentCaptor.capture());

        Course captureCourse = courseArgumentCaptor.getValue();

        assertEquals(new Long(1l), captureCourse.getId());
        assertEquals(0, captureCourse.getChapters().size());

        verify(chapterDataService).update(chapterArgumentCaptor.capture());
        verify(lessonDataService).update(lessonArgumentCaptor.capture());
        verify(quizDataService).update(quizArgumentCaptor.capture());

        Chapter capturedChapter = chapterArgumentCaptor.getValue();
        Lesson capturedLesson = lessonArgumentCaptor.getValue();
        Quiz capturedQuiz = quizArgumentCaptor.getValue();

        assertNull(capturedChapter.getQuiz());
        assertNull(capturedChapter.getCourse());
        Assert.assertEquals(0, capturedChapter.getLessons().size());
        assertNull(capturedLesson.getChapter());
        assertNull(capturedQuiz.getChapter());
    }

    @Test
    public void shouldCreateCorrectStructure() {
        CourseUnitListWrapper coursesToUpdate = new CourseUnitListWrapper();
        coursesToUpdate.add(new CourseUnitDto(1l, Constants.COURSE, CourseUnitState.Active.toString(),
                asList(
                        new ChapterUnitDto(11l, Constants.CHAPTER, CourseUnitState.Active.toString(),
                                asList(
                                        new CourseUnitDto(13l, Constants.LESSON, CourseUnitState.Active.toString(), null, Constants.LESSON)
                                ),
                                new CourseUnitDto(15l, Constants.QUIZ, CourseUnitState.Active.toString(), null, Constants.QUIZ))
                ), Constants.COURSE));

        Quiz quiz = new Quiz(Constants.QUIZ, CourseUnitState.Active, Constants.QUIZ, Constants.QUIZ, null);
        quiz.setId(15l);
        Lesson lesson = new Lesson(Constants.LESSON, CourseUnitState.Active, Constants.LESSON, Constants.LESSON, null);
        lesson.setId(13l);
        Chapter chapter = new Chapter(Constants.CHAPTER , CourseUnitState.Active, Constants.CHAPTER, Constants.CHAPTER, null, null, null);
        chapter.setId(11l);
        Course course = new Course(Constants.COURSE, CourseUnitState.Active, Constants.COURSE, Constants.COURSE, null, null);
        course.setId(1l);

        when(courseDataService.detachedCopy(courseDataService.findCourseById(1l))).thenReturn(course);
        when(chapterDataService.findChapterById(11l)).thenReturn(chapter);
        when(lessonDataService.findLessonById(13l)).thenReturn(lesson);
        when(quizDataService.findQuizById(15l)).thenReturn(quiz);


        courseStructureService.updateCourseStructure(coursesToUpdate);

        verify(courseDataService).update(courseArgumentCaptor.capture());

        Course captureCourse = courseArgumentCaptor.getValue();

        assertEquals(new Long(1l), captureCourse.getId());
        assertEquals(1, captureCourse.getChapters().size());

        Chapter courseChapter = captureCourse.getChapters().get(0);
        assertEquals(new Long(11l), courseChapter.getId());
        assertEquals(1, courseChapter.getLessons().size());
        assertNotNull(courseChapter.getQuiz());
    }

    @Test(expected = CourseUnitNotFoundException.class)
    public void shouldCheckIfUnitExist() {
        CourseUnitListWrapper coursesToUpdate = new CourseUnitListWrapper();

        coursesToUpdate.add(new CourseUnitDto(1l, "sample_1", CourseUnitState.Active.toString(), null, Constants.COURSE));
        coursesToUpdate.add(new CourseUnitDto(2l, "sample_2", CourseUnitState.Active.toString(), null, Constants.COURSE));

        when(courseDataService.findById(1l)).thenReturn(null);
        courseStructureService.updateCourseStructure(coursesToUpdate);
    }
}
