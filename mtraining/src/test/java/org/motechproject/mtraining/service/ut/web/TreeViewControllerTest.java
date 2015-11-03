package org.motechproject.mtraining.service.ut.web;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.mtraining.domain.Chapter;
import org.motechproject.mtraining.domain.Course;
import org.motechproject.mtraining.domain.CourseUnitState;
import org.motechproject.mtraining.domain.Lesson;
import org.motechproject.mtraining.domain.Quiz;
import org.motechproject.mtraining.dto.ChapterUnitDto;
import org.motechproject.mtraining.dto.CourseUnitDto;
import org.motechproject.mtraining.dto.CourseUnitListWrapper;
import org.motechproject.mtraining.service.CourseStructureService;
import org.motechproject.mtraining.service.MTrainingService;
import org.motechproject.mtraining.util.Constants;
import org.motechproject.mtraining.web.TreeViewController;
import org.springframework.http.MediaType;
import org.springframework.test.web.server.MockMvc;
import org.springframework.test.web.server.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class TreeViewControllerTest {

    @Mock
    MTrainingService mTrainingService;

    @Mock
    CourseStructureService courseStructureService;

    @InjectMocks
    private TreeViewController entityController = new TreeViewController();

    private MockMvc controller;

    @Captor
    ArgumentCaptor<List<CourseUnitDto>> coursesDtoCaptor;

    @Before
    public void setUp() {
        controller = MockMvcBuilders.standaloneSetup(entityController).build();
    }

    @Test
    public void shouldReturnCourses() throws Exception {
        List<CourseUnitDto> expected = generateCourses("courses_");

        Quiz quiz = new Quiz("courses_quiz", CourseUnitState.Active, "content_1", "Description_1", null);
        quiz.setId(3l);

        Lesson lesson = new Lesson("courses_lesson_1", CourseUnitState.Active, "content_1", "Description_1", null);
        lesson.setId(1l);

        Chapter chapter1 = new Chapter("courses_chapter_1", CourseUnitState.Active, "content_1", "Description_1", null, asList(lesson), quiz);
        Chapter chapter2 = new Chapter("courses_chapter_1", CourseUnitState.Active, "content_1", "Description_1", null, asList(lesson), null);
        chapter1.setId(1l);
        chapter2.setId(2l);

        Course course1 = new Course("courses_course_1", CourseUnitState.Active, "content_1", "Description_1", null, asList(chapter1, chapter2));
        Course course2 = new Course("courses_course_2", CourseUnitState.Active, "content_2", "Description_2", null, asList(chapter1));
        course1.setId(1l);
        course2.setId(2l);

        when(mTrainingService.getAllCourses()).thenReturn(asList(course1, course2));
        controller.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(content().string(new ObjectMapper().writeValueAsString(expected)));
    }

    @Test
    public void shouldReturnChapters() throws Exception {
        List<CourseUnitDto> expected = generateChapters("chapters_", true);

        Quiz quiz = new Quiz("chapters_quiz", CourseUnitState.Active, "content_1", "Description_1", null);
        quiz.setId(3l);

        Lesson lesson = new Lesson("chapters_lesson_1", CourseUnitState.Active, "content_1", "Description_1", null);
        lesson.setId(1l);

        Chapter chapter1 = new Chapter("chapters_chapter_1", CourseUnitState.Active, "content_1", "Description_1", null, asList(lesson), quiz);
        Chapter chapter2 = new Chapter("chapters_chapter_1", CourseUnitState.Active, "content_1", "Description_1", null, asList(lesson), null);
        chapter1.setId(1l);
        chapter2.setId(2l);

        when(mTrainingService.getUnusedChapters()).thenReturn(asList(chapter1, chapter2));
        controller.perform(get("/chapters"))
                .andExpect(status().isOk())
                .andExpect(content().string(new ObjectMapper().writeValueAsString(expected)));
    }

    @Test
    public void shouldReturnLessons() throws Exception {
        List<CourseUnitDto> expected = generateLessons("lessons_");
        Lesson lesson = new Lesson("lessons_lesson_1", CourseUnitState.Active, "content_1", "Description_1", null);
        lesson.setId(1l);

        when(mTrainingService.getUnusedLessons()).thenReturn(asList(lesson));
        controller.perform(get("/lessons"))
                .andExpect(status().isOk())
                .andExpect(content().string(new ObjectMapper().writeValueAsString(expected)));
    }

    @Test
    public void shouldReturnQuizzes() throws Exception {
        List<CourseUnitDto> expected = asList(generateQuiz("quizzes_"));
        Quiz quiz = new Quiz("quizzes_quiz", CourseUnitState.Active, "content_1", "Description_1", null);
        quiz.setId(3l);

        when(mTrainingService.getUnusedQuizzes()).thenReturn(asList(quiz));
        controller.perform(get("/quizzes"))
                .andExpect(status().isOk())
                .andExpect(content().string(new ObjectMapper().writeValueAsString(expected)));
    }

    @Test
    public void shouldDeserializeCoursesStructureAndUpdateStructure() throws Exception {
        CourseUnitListWrapper coursesToUpdate = generateCourses("update_");

        controller.perform(post("/updateCourses")
                .body(new ObjectMapper().writeValueAsBytes(coursesToUpdate))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(courseStructureService).updateCourseStructure(coursesDtoCaptor.capture());
        List<CourseUnitDto> courses = coursesDtoCaptor.getValue();

        assertNotNull(courses);
        assertEquals(2, courses.size());
        assertEquals("update_course_1", courses.get(0).getName());
        assertEquals("update_course_2", courses.get(1).getName());

        List<CourseUnitDto> chapters = courses.get(0).getUnits();
        assertNotNull(chapters);
        assertEquals(2, chapters.size());

        CourseUnitDto quiz = ((ChapterUnitDto) chapters.get(0)).getQuiz();
        assertNotNull(quiz);
        assertEquals(Constants.QUIZ, quiz.getType());
        assertEquals("update_quiz", quiz.getName());
        quiz = ((ChapterUnitDto) chapters.get(1)).getQuiz();
        assertNull(quiz);

        List<CourseUnitDto> lessons = chapters.get(0).getUnits();
        assertNotNull(lessons);
        assertEquals(1, lessons.size());
        assertEquals(Constants.LESSON, lessons.get(0).getType());
        assertEquals("update_lesson_1", lessons.get(0).getName());

        chapters = courses.get(1).getUnits();
        assertNotNull(chapters);
        assertEquals(1, chapters.size());
    }

    private CourseUnitListWrapper generateCourses(String prefix) {
        CourseUnitListWrapper courses = new CourseUnitListWrapper();
        CourseUnitDto course1 = new CourseUnitDto(1l, prefix + "course_1", CourseUnitState.Active.toString(), generateChapters(prefix, true), Constants.COURSE);
        CourseUnitDto course2 = new CourseUnitDto(2l, prefix + "course_2", CourseUnitState.Active.toString(), generateChapters(prefix, false), Constants.COURSE);
        courses.add(course1);
        courses.add(course2);
        return courses;
    }

    private List<CourseUnitDto> generateChapters(String prefix, boolean two) {
        List<CourseUnitDto> chapters = new ArrayList<>();
        CourseUnitDto chapter1 = new ChapterUnitDto(1l, prefix + "chapter_1", CourseUnitState.Active.toString(), generateLessons(prefix), generateQuiz(prefix));
        chapters.add(chapter1);
        if (two) {
            CourseUnitDto chapter2 = new ChapterUnitDto(2l, prefix + "chapter_1", CourseUnitState.Active.toString(), generateLessons(prefix), null);
            chapters.add(chapter2);
        }
        return chapters;
    }

    private List<CourseUnitDto> generateLessons(String prefix) {
        List<CourseUnitDto> lessons = new ArrayList<>();
        CourseUnitDto lesson = new CourseUnitDto(1l, prefix + "lesson_1", CourseUnitState.Active.toString(), null, Constants.LESSON);
        lessons.add(lesson);
        return lessons;
    }

    private CourseUnitDto generateQuiz(String prefix) {
        return new CourseUnitDto(3l, prefix + "quiz", CourseUnitState.Active.toString(), null, Constants.QUIZ);
    }
}
