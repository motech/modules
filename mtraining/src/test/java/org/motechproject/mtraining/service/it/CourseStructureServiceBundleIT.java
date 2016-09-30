package org.motechproject.mtraining.service.it;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.mtraining.domain.Chapter;
import org.motechproject.mtraining.domain.Course;
import org.motechproject.mtraining.domain.CourseUnitState;
import org.motechproject.mtraining.domain.Lesson;
import org.motechproject.mtraining.domain.Quiz;
import org.motechproject.mtraining.dto.ChapterUnitDto;
import org.motechproject.mtraining.dto.CourseUnitDto;
import org.motechproject.mtraining.repository.ChapterDataService;
import org.motechproject.mtraining.repository.CourseDataService;
import org.motechproject.mtraining.repository.LessonDataService;
import org.motechproject.mtraining.repository.QuizDataService;
import org.motechproject.mtraining.service.CourseStructureService;
import org.motechproject.mtraining.service.MTrainingService;
import org.motechproject.mtraining.util.Constants;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;
import static java.util.Arrays.asList;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class CourseStructureServiceBundleIT extends BasePaxIT {

    private static final String COURSE_1 = "course_1";
    private static final String COURSE_2 = "course_2";
    private static final String COURSE_3 = "course_3";
    private static final String COURSE_4 = "course_4";
    private static final String CHAPTER_1 = "chapter_1";
    private static final String CHAPTER_2 = "chapter_2";
    private static final String CHAPTER_3 = "chapter_3";
    private static final String CHAPTER_4 = "chapter_4";
    private static final String CHAPTER_5 = "chapter_5";
    private static final String CHAPTER_6 = "chapter_6";
    private static final String LESSON_1 = "lesson_1";
    private static final String LESSON_2 = "lesson_2";
    private static final String LESSON_3 = "lesson_3";
    private static final String LESSON_4 = "lesson_4";
    private static final String LESSON_5 = "lesson_5";
    private static final String LESSON_6 = "lesson_6";
    private static final String LESSON_7 = "lesson_7";
    private static final String LESSON_8 = "lesson_8";
    private static final String LESSON_9 = "lesson_9";
    private static final String QUIZ_1 = "quiz_1";
    private static final String QUIZ_2 = "quiz_2";
    private static final String QUIZ_3 = "quiz_3";
    private static final String QUIZ_4 = "quiz_4";
    private static final String QUIZ_5 = "quiz_5";
    private static final String QUIZ_6 = "quiz_6";

    private static final String ACTIVE = "Active";
    private static final String PENDING = "Pending";
    private static final String INACTIVE = "Inactive";


    private Map<String, Long> idMap;

    @Inject
    private ChapterDataService chapterDataService;

    @Inject
    private CourseDataService courseDataService;

    @Inject
    private LessonDataService lessonDataService;

    @Inject
    private QuizDataService quizDataService;

    @Inject
    private MTrainingService mTrainingService;

    @Inject
    private CourseStructureService courseStructureService;

    // Original structure(All nodes are active)
    // course_1
    //     - chapter_1
    //          - lesson_1
    //          - lesson_2
    //          - quiz_1
    //     - chapter_2
    //          - lesson_3
    //          - quiz_2
    // course_2
    //     - chapter_3
    //          - lesson_4
    //          - lesson_5
    //          - quiz_3
    // course_3
    // course_4
    //
    // Unused units:
    // chapter_4, chapter_5, chapter_6, lesson_6, lesson_7, lesson_8,
    // lesson_9, quiz_4, quiz_5, quiz_6
    @Before
    public void setUp() {
        getLogger().info("setup");
        lessonDataService.deleteAll();
        chapterDataService.deleteAll();
        courseDataService.deleteAll();
        quizDataService.deleteAll();
    }

    // Structure after test 1:
    // course_1
    //     - chapter_1
    //          - lesson_1 (Pending)
    //          - lesson_2 (Pending)
    //          - lesson_4
    //     - chapter_2
    //          - lesson_3
    //          - quiz_2 (Inactive)
    // course_2 (Inactive)
    // course_3
    // course_4
    //
    // Unused units:
    // chapter_3, chapter_4, chapter_5, chapter_6, lesson_5, lesson_6, lesson_7, lesson_8,
    // lesson_9, quiz_1, quiz_3, quiz_4, quiz_5, quiz_6
    @Test
    public void updateCourseStructureTest1() throws Exception {
        getLogger().info("Creating basic structure");
        setUpData();

        // preparing dto data
        List<CourseUnitDto> coursesToUpdate = new ArrayList<>();

        coursesToUpdate.add(new CourseUnitDto(idMap.get(COURSE_1), COURSE_1, ACTIVE,
                asList(
                        new ChapterUnitDto(idMap.get(CHAPTER_1), CHAPTER_1, ACTIVE,
                                asList(
                                        new CourseUnitDto(idMap.get(LESSON_1), LESSON_1, PENDING, null, Constants.LESSON),
                                        new CourseUnitDto(idMap.get(LESSON_2), LESSON_2, PENDING, null, Constants.LESSON),
                                        new CourseUnitDto(idMap.get(LESSON_4), LESSON_4, ACTIVE, null, Constants.LESSON)
                                ),
                                null),
                        new ChapterUnitDto(idMap.get(CHAPTER_2), CHAPTER_2, ACTIVE,
                                asList(
                                        new CourseUnitDto(idMap.get(LESSON_3), LESSON_3, ACTIVE, null, Constants.LESSON)
                                ),
                                new CourseUnitDto(idMap.get(QUIZ_2), QUIZ_2, INACTIVE, null, Constants.QUIZ))
                ), Constants.COURSE));
        coursesToUpdate.add(new CourseUnitDto(idMap.get(COURSE_2), COURSE_2, INACTIVE,
                null, Constants.COURSE));

        getLogger().info("Updating course structure");
        courseStructureService.updateCourseStructure(coursesToUpdate);

        // verify structure
        Course course = courseDataService.findById(idMap.get(COURSE_1));
        assertNotNull(course);
        assertEquals(2, course.getChapters().size());

        Chapter chapter = course.getChapters().get(0);
        assertNull(chapter.getQuiz());
        verifyLessons(chapter, 3, asList(LESSON_1, LESSON_2, LESSON_4), asList(CourseUnitState.Pending, CourseUnitState.Pending, CourseUnitState.Active));

        chapter = course.getChapters().get(1);
        verifyLessons(chapter, 1, asList(LESSON_3), asList(CourseUnitState.Active));
        verifyQuiz(chapter, QUIZ_2, CourseUnitState.Inactive);

        course = courseDataService.findById(idMap.get(COURSE_2));
        assertNotNull(course);
        assertEquals(0, course.getChapters().size());
        assertEquals(INACTIVE, course.getState().toString());

        course = courseDataService.findById(idMap.get(COURSE_3));
        assertNotNull(course);
        assertEquals(0, course.getChapters().size());

        course = courseDataService.findById(idMap.get(COURSE_4));
        assertNotNull(course);
        assertEquals(0, course.getChapters().size());

        // verify unused units
        verifyUnused(asList(QUIZ_1, QUIZ_3, QUIZ_4, QUIZ_5, QUIZ_6), asList(LESSON_5, LESSON_6, LESSON_7, LESSON_8, LESSON_9),
                asList(CHAPTER_3, CHAPTER_4, CHAPTER_5, CHAPTER_6));
    }


    // Structure after test 2:
    // course_1
    //     - chapter_1
    //     - chapter_2
    //          - lesson_3
    //          - quiz_2 (Inactive)
    // course_2 (Inactive)
    // course_3
    // course_4
    //
    // Unused units:
    // chapter_3, chapter_4, chapter_5, chapter_6, lesson_1, lesson_2, lesson_4, lesson_5,
    // lesson_6, lesson_7, lesson_8, lesson_9, quiz_1, quiz_3, quiz_4, quiz_5, quiz_6
    @Test
    public void updateCourseStructureTest2() throws Exception {
        getLogger().info("Creating basic structure");
        setUpData();

        // preparing dto data
        List<CourseUnitDto> coursesToUpdate = new ArrayList<>();

        coursesToUpdate.add(new CourseUnitDto(idMap.get(COURSE_1), COURSE_1, ACTIVE,
                asList(
                        new ChapterUnitDto(idMap.get(CHAPTER_1), CHAPTER_1, ACTIVE, null, null),
                        new ChapterUnitDto(idMap.get(CHAPTER_2), CHAPTER_2, ACTIVE,
                                asList(
                                        new CourseUnitDto(idMap.get(LESSON_3), LESSON_3, ACTIVE, null, Constants.LESSON)
                                ),
                                new CourseUnitDto(idMap.get(QUIZ_2), QUIZ_2, INACTIVE, null, Constants.QUIZ))
                ), Constants.COURSE));
        coursesToUpdate.add(new CourseUnitDto(idMap.get(COURSE_2), COURSE_2, INACTIVE,
                null, Constants.COURSE));

        String json = new ObjectMapper().writeValueAsString(coursesToUpdate);

        getLogger().info("Updating course structure");
        courseStructureService.updateCourseStructure(coursesToUpdate);

        // verify structure
        Course course = courseDataService.findById(idMap.get(COURSE_1));
        assertNotNull(course);
        assertEquals(2, course.getChapters().size());

        Chapter chapter = course.getChapters().get(0);
        assertEquals(CHAPTER_1, chapter.getName());
        assertNull(chapter.getQuiz());
        assertEquals(0, chapter.getLessons().size());

        chapter = course.getChapters().get(1);
        assertEquals(CHAPTER_2, chapter.getName());
        verifyLessons(chapter, 1, asList(LESSON_3), asList(CourseUnitState.Active));
        verifyQuiz(chapter, QUIZ_2, CourseUnitState.Inactive);

        course = courseDataService.findById(idMap.get(COURSE_2));
        assertNotNull(course);
        assertEquals(0, course.getChapters().size());
        assertEquals(INACTIVE, course.getState().toString());

        course = courseDataService.findById(idMap.get(COURSE_3));
        assertNotNull(course);
        assertEquals(0, course.getChapters().size());
        assertEquals(ACTIVE, course.getState().toString());

        course = courseDataService.findById(idMap.get(COURSE_4));
        assertNotNull(course);
        assertEquals(0, course.getChapters().size());

        // verify unused units
        verifyUnused(asList(QUIZ_1, QUIZ_3, QUIZ_4, QUIZ_5, QUIZ_6), asList(LESSON_1, LESSON_2, LESSON_4, LESSON_5,
                LESSON_6, LESSON_7, LESSON_8, LESSON_9), asList(CHAPTER_3, CHAPTER_4, CHAPTER_5, CHAPTER_6));
    }

    // Structure after test 3:
    // course_1
    //     - chapter_1
    //          - lesson_1
    //          - quiz_1
    //     - chapter_2
    //          - lesson_3
    //          - quiz_2
    // course_2
    //     - chapter_3 (Inactive)
    //          - lesson_4
    //          - quiz_3
    // course_3
    //     - chapter_4 (Inactive)
    //          - quiz_4
    // course_4
    //     - chapter_5 (Inactive)
    //          - lesson_2
    //          - lesson_5
    //          - lesson_8
    //
    // Unused units:
    // chapter_6, lesson_6, lesson_7, lesson_9, quiz_5, quiz_6
    @Test
    public void updateCourseStructureTest3() throws Exception {
        getLogger().info("Creating basic structure");
        setUpData();

        // preparing dto data
        List<CourseUnitDto> coursesToUpdate = new ArrayList<>();

        coursesToUpdate.add(new CourseUnitDto(idMap.get(COURSE_1), COURSE_1, ACTIVE,
                asList(
                        new ChapterUnitDto(idMap.get(CHAPTER_1), CHAPTER_1, ACTIVE,
                                asList(
                                        new CourseUnitDto(idMap.get(LESSON_1), LESSON_1, ACTIVE, null, Constants.LESSON)
                                ),
                                new CourseUnitDto(idMap.get(QUIZ_1), QUIZ_1, ACTIVE, null, Constants.QUIZ)),
                        new ChapterUnitDto(idMap.get(CHAPTER_2), CHAPTER_2, ACTIVE,
                                asList(
                                        new CourseUnitDto(idMap.get(LESSON_3), LESSON_3, ACTIVE, null, Constants.LESSON)
                                ),
                                new CourseUnitDto(idMap.get(QUIZ_2), QUIZ_2, ACTIVE, null, Constants.QUIZ))
                ), Constants.COURSE));
        coursesToUpdate.add(new CourseUnitDto(idMap.get(COURSE_2), COURSE_2, ACTIVE,
                asList(
                        new ChapterUnitDto(idMap.get(CHAPTER_3), CHAPTER_3, INACTIVE,
                                asList(
                                        new CourseUnitDto(idMap.get(LESSON_4), LESSON_4, ACTIVE, null, Constants.LESSON)
                                ),
                                new CourseUnitDto(idMap.get(QUIZ_3), QUIZ_3, ACTIVE, null, Constants.QUIZ))
                ), Constants.COURSE));
        coursesToUpdate.add(new CourseUnitDto(idMap.get(COURSE_3), COURSE_3, ACTIVE,
                asList(
                        new ChapterUnitDto(idMap.get(CHAPTER_4), CHAPTER_4, INACTIVE, null,
                                new CourseUnitDto(idMap.get(QUIZ_4), QUIZ_4, ACTIVE, null, Constants.QUIZ))
                ), Constants.COURSE));
        coursesToUpdate.add(new CourseUnitDto(idMap.get(COURSE_4), COURSE_4, ACTIVE,
                asList(
                        new ChapterUnitDto(idMap.get(CHAPTER_5), CHAPTER_5, INACTIVE,
                                asList(
                                        new CourseUnitDto(idMap.get(LESSON_2), LESSON_2, ACTIVE, null, Constants.LESSON),
                                        new CourseUnitDto(idMap.get(LESSON_5), LESSON_5, ACTIVE, null, Constants.LESSON),
                                        new CourseUnitDto(idMap.get(LESSON_8), LESSON_8, ACTIVE, null, Constants.LESSON)
                                ), null)
                ), Constants.COURSE));

        getLogger().info("Updating course structure");
        courseStructureService.updateCourseStructure(coursesToUpdate);

        // verify structure
        Course course = courseDataService.findById(idMap.get(COURSE_1));
        assertNotNull(course);
        assertEquals(2, course.getChapters().size());

        Chapter chapter = course.getChapters().get(0);
        assertEquals(CourseUnitState.Active, chapter.getState());
        verifyLessons(chapter, 1, asList(LESSON_1), asList(CourseUnitState.Active));
        verifyQuiz(chapter, QUIZ_1, CourseUnitState.Active);

        chapter = course.getChapters().get(1);
        assertEquals(CourseUnitState.Active, chapter.getState());
        verifyLessons(chapter, 1, asList(LESSON_3), asList(CourseUnitState.Active));
        verifyQuiz(chapter, QUIZ_2, CourseUnitState.Active);

        course = courseDataService.findById(idMap.get(COURSE_2));
        assertNotNull(course);
        assertEquals(1, course.getChapters().size());

        chapter = course.getChapters().get(0);
        assertEquals(CourseUnitState.Inactive, chapter.getState());
        verifyLessons(chapter, 1, asList(LESSON_4), asList(CourseUnitState.Active));
        verifyQuiz(chapter, QUIZ_3, CourseUnitState.Active);

        course = courseDataService.findById(idMap.get(COURSE_3));
        assertNotNull(course);
        assertEquals(1, course.getChapters().size());

        chapter = course.getChapters().get(0);
        assertEquals(CourseUnitState.Inactive, chapter.getState());
        assertEquals(CHAPTER_4, chapter.getName());
        assertEquals(0, chapter.getLessons().size());
        verifyQuiz(chapter, QUIZ_4, CourseUnitState.Active);

        course = courseDataService.findById(idMap.get(COURSE_4));
        assertNotNull(course);
        assertEquals(1, course.getChapters().size());

        chapter = course.getChapters().get(0);
        assertEquals(CourseUnitState.Inactive, chapter.getState());
        assertEquals(CHAPTER_5, chapter.getName());
        verifyLessons(chapter, 3, asList(LESSON_2, LESSON_5, LESSON_8), asList(CourseUnitState.Active, CourseUnitState.Active, CourseUnitState.Active));
        assertNull(chapter.getQuiz());

        // verify unused units
        verifyUnused(asList(QUIZ_5, QUIZ_6), asList(LESSON_6, LESSON_7, LESSON_9), asList(CHAPTER_6));
    }

    // Structure after test 4:
    // course_1
    // course_2
    // course_3 (Pending)
    //     - chapter_1 (Pending)
    //          - lesson_1 (Pending)
    //          - lesson_2 (Pending)
    //          - quiz_1 (Pending)
    //     - chapter_2 (Inactive)
    //          - lesson_3 (Inactive)
    //          - quiz_2 (Inactive)
    //     - chapter_3
    //          - lesson_4
    //          - lesson_5
    //          - quiz_3
    //     - chapter_5  (Pending)
    //          - lesson_6 (Inactive)
    //          - lesson_7 (Inactive)
    //          - quiz_5 (Inactive)
    //     - chapter_6
    //          - lesson_8
    //          - lesson_9
    //          - quiz_6
    // course_4
    //
    // Unused units:
    // chapter_4, quiz_4
    @Test
    public void updateCourseStructureTest4() throws Exception {
        getLogger().info("Creating basic structure");
        setUpData();

        // preparing dto data
        List<CourseUnitDto> coursesToUpdate = new ArrayList<>();

        coursesToUpdate.add(new CourseUnitDto(idMap.get(COURSE_1), COURSE_1, ACTIVE,
                null, Constants.COURSE));
        coursesToUpdate.add(new CourseUnitDto(idMap.get(COURSE_2), COURSE_2, ACTIVE,
                null, Constants.COURSE));
        coursesToUpdate.add(new CourseUnitDto(idMap.get(COURSE_3), COURSE_3, PENDING,
                asList(
                        new ChapterUnitDto(idMap.get(CHAPTER_1), CHAPTER_1, PENDING,
                                asList(
                                        new CourseUnitDto(idMap.get(LESSON_1), LESSON_1, PENDING, null, Constants.LESSON),
                                        new CourseUnitDto(idMap.get(LESSON_2), LESSON_2, PENDING, null, Constants.LESSON)
                                ),
                                new CourseUnitDto(idMap.get(QUIZ_1), QUIZ_1, PENDING, null, Constants.QUIZ)),
                        new ChapterUnitDto(idMap.get(CHAPTER_2), CHAPTER_2, INACTIVE,
                                asList(
                                        new CourseUnitDto(idMap.get(LESSON_3), LESSON_3, INACTIVE, null, Constants.LESSON)
                                ),
                                new CourseUnitDto(idMap.get(QUIZ_2), QUIZ_2, INACTIVE, null, Constants.QUIZ)),
                        new ChapterUnitDto(idMap.get(CHAPTER_3), CHAPTER_3, ACTIVE,
                                asList(
                                        new CourseUnitDto(idMap.get(LESSON_4), LESSON_4, ACTIVE, null, Constants.LESSON),
                                        new CourseUnitDto(idMap.get(LESSON_5), LESSON_5, ACTIVE, null, Constants.LESSON)
                                ),
                                new CourseUnitDto(idMap.get(QUIZ_3), QUIZ_3, ACTIVE, null, Constants.QUIZ)),
                        new ChapterUnitDto(idMap.get(CHAPTER_5), CHAPTER_5, PENDING,
                                asList(
                                        new CourseUnitDto(idMap.get(LESSON_6), LESSON_6, INACTIVE, null, Constants.LESSON),
                                        new CourseUnitDto(idMap.get(LESSON_7), LESSON_7, INACTIVE, null, Constants.LESSON)
                                ),
                                new CourseUnitDto(idMap.get(QUIZ_5), QUIZ_5, INACTIVE, null, Constants.QUIZ)),
                        new ChapterUnitDto(idMap.get(CHAPTER_6), CHAPTER_6, ACTIVE,
                                asList(
                                        new CourseUnitDto(idMap.get(LESSON_8), LESSON_8, ACTIVE, null, Constants.LESSON),
                                        new CourseUnitDto(idMap.get(LESSON_9), LESSON_9, ACTIVE, null, Constants.LESSON)
                                ),
                                new CourseUnitDto(idMap.get(QUIZ_6), QUIZ_6, ACTIVE, null, Constants.QUIZ))
                ), Constants.COURSE));
        coursesToUpdate.add(new CourseUnitDto(idMap.get(COURSE_4), COURSE_4, ACTIVE,
                null, Constants.COURSE));


        getLogger().info("Updating course structure");
        courseStructureService.updateCourseStructure(coursesToUpdate);

        Course course = courseDataService.findById(idMap.get(COURSE_1));
        assertNotNull(course);
        assertEquals(0, course.getChapters().size());

        course = courseDataService.findById(idMap.get(COURSE_2));
        assertNotNull(course);
        assertEquals(0, course.getChapters().size());

        course = courseDataService.findById(idMap.get(COURSE_3));
        assertNotNull(course);
        assertEquals(5, course.getChapters().size());

        List<String> chapters = extract(course.getChapters(), on(Chapter.class).getName());
        assertEquals(asList(CHAPTER_1, CHAPTER_2, CHAPTER_3, CHAPTER_5, CHAPTER_6), chapters);
        List<CourseUnitState> states = extract(course.getChapters(), on(Chapter.class).getState());
        assertEquals(asList(CourseUnitState.Pending, CourseUnitState.Inactive, CourseUnitState.Active, CourseUnitState.Pending,
                CourseUnitState.Active), states);

        Chapter chapter = course.getChapters().get(0);
        verifyLessons(chapter, 2, asList(LESSON_1, LESSON_2), asList(CourseUnitState.Pending, CourseUnitState.Pending));
        verifyQuiz(chapter, QUIZ_1, CourseUnitState.Pending);

        chapter = course.getChapters().get(1);
        verifyLessons(chapter, 1, asList(LESSON_3), asList(CourseUnitState.Inactive));
        verifyQuiz(chapter, QUIZ_2, CourseUnitState.Inactive);

        chapter = course.getChapters().get(2);
        verifyLessons(chapter, 2, asList(LESSON_4, LESSON_5), asList(CourseUnitState.Active, CourseUnitState.Active));
        verifyQuiz(chapter, QUIZ_3, CourseUnitState.Active);

        chapter = course.getChapters().get(3);
        verifyLessons(chapter, 2, asList(LESSON_6, LESSON_7), asList(CourseUnitState.Inactive, CourseUnitState.Inactive));
        verifyQuiz(chapter, QUIZ_5, CourseUnitState.Inactive);

        chapter = course.getChapters().get(4);
        verifyLessons(chapter, 2, asList(LESSON_8, LESSON_9), asList(CourseUnitState.Active, CourseUnitState.Active));
        verifyQuiz(chapter, QUIZ_6, CourseUnitState.Active);

        course = courseDataService.findById(idMap.get(COURSE_4));
        assertNotNull(course);
        assertEquals(0, course.getChapters().size());

        // verify unused units
        verifyUnused(asList(QUIZ_4), asList(), asList(CHAPTER_4));
    }

    // Structure after test 5:
    // course_1
    // course_2
    // course_3
    // course_4
    //
    // Unused units:
    // chapter_1, chapter_2, chapter_3, chapter_4, chapter_5, chapter_6,
    // lesson_1, lesson_2, lesson_3, lesson_4, lesson_5, lesson_6, lesson_7, lesson_8, lesson_9,
    // quiz_1, quiz_2, quiz_3, quiz_4, quiz_5, quiz_6
    @Test
    public void updateCourseStructureTest5() throws Exception {
        getLogger().info("Creating basic structure");
        setUpData();

        // preparing dto data
        List<CourseUnitDto> coursesToUpdate = new ArrayList<>();

        coursesToUpdate.add(new CourseUnitDto(idMap.get(COURSE_1), COURSE_1, ACTIVE,
                null, Constants.COURSE));
        coursesToUpdate.add(new CourseUnitDto(idMap.get(COURSE_2), COURSE_2, ACTIVE,
                null, Constants.COURSE));
        coursesToUpdate.add(new CourseUnitDto(idMap.get(COURSE_3), COURSE_3, ACTIVE,
                null, Constants.COURSE));
        coursesToUpdate.add(new CourseUnitDto(idMap.get(COURSE_4), COURSE_4, ACTIVE,
                null, Constants.COURSE));

        getLogger().info("Updating course structure");
        courseStructureService.updateCourseStructure(coursesToUpdate);

        Course course = courseDataService.findById(idMap.get(COURSE_1));
        assertNotNull(course);
        assertEquals(0, course.getChapters().size());

        course = courseDataService.findById(idMap.get(COURSE_2));
        assertNotNull(course);
        assertEquals(0, course.getChapters().size());
        course = courseDataService.findById(idMap.get(COURSE_3));
        assertNotNull(course);
        assertEquals(0, course.getChapters().size());

        course = courseDataService.findById(idMap.get(COURSE_4));
        assertNotNull(course);
        assertEquals(0, course.getChapters().size());

        // verify unused units
        verifyUnused(asList(QUIZ_1, QUIZ_2, QUIZ_3, QUIZ_4, QUIZ_5, QUIZ_6), asList(LESSON_1, LESSON_2, LESSON_3, LESSON_4,
                LESSON_5, LESSON_6, LESSON_7, LESSON_8, LESSON_9), asList(CHAPTER_1, CHAPTER_2, CHAPTER_3, CHAPTER_4, CHAPTER_5, CHAPTER_6));
    }

    // Structure after test 6:
    // course_1
    //     - chapter_1
    //          - lesson_1
    //          - lesson_2
    //          - quiz_2
    //     - chapter_2
    //          - lesson_3
    //          - quiz_1
    // course_2
    //     - chapter_3
    //          - lesson_4
    //          - lesson_5
    //          - quiz_5
    // course_3
    //     - new_chapter (it contains lesson_10, and quiz_7 which should be disconnected)
    //          - lesson_6
    // course_4
    //
    // Unused units:
    // chapter_4, chapter_5, chapter_6, lesson_7, lesson_8, lesson_10
    // lesson_9, quiz_3, quiz_4, quiz_6, quiz_7
    @Test
    public void updateCourseStructureTest6() throws Exception {
        getLogger().info("Creating basic structure");
        setUpData();


        Quiz quiz7 = new Quiz("quiz_7", CourseUnitState.Active, "content_7", "Description_7", null);
        quizDataService.create(quiz7);

        Lesson lesson10 = new Lesson("lesson_10", CourseUnitState.Active, "content_10", "Description_10", null);
        lessonDataService.create(lesson10);

        List<Lesson> lessons = new ArrayList<>();
        lessons.add(lesson10);
        Chapter newChapter = new Chapter("new_chapter", CourseUnitState.Active, "content_1", "Description_1", null, lessons, quiz7);
        chapterDataService.create(newChapter);
        // preparing dto data
        List<CourseUnitDto> coursesToUpdate = new ArrayList<>();

        coursesToUpdate.add(new CourseUnitDto(idMap.get(COURSE_1), COURSE_1, ACTIVE,
                asList(
                        new ChapterUnitDto(idMap.get(CHAPTER_1), CHAPTER_1, ACTIVE,
                                asList(
                                        new CourseUnitDto(idMap.get(LESSON_1), LESSON_1, ACTIVE, null, Constants.LESSON),
                                        new CourseUnitDto(idMap.get(LESSON_2), LESSON_2, ACTIVE, null, Constants.LESSON)
                                ),
                                new CourseUnitDto(idMap.get(QUIZ_2), QUIZ_2, ACTIVE, null, Constants.QUIZ)),
                        new ChapterUnitDto(idMap.get(CHAPTER_2), CHAPTER_2, ACTIVE,
                                asList(
                                        new CourseUnitDto(idMap.get(LESSON_3), LESSON_3, ACTIVE, null, Constants.LESSON)
                                ),
                                new CourseUnitDto(idMap.get(QUIZ_1), QUIZ_1, ACTIVE, null, Constants.QUIZ))
                ), Constants.COURSE));
        coursesToUpdate.add(new CourseUnitDto(idMap.get(COURSE_2), COURSE_2, ACTIVE,
                asList(
                        new ChapterUnitDto(idMap.get(CHAPTER_3), CHAPTER_3, ACTIVE,
                                asList(
                                        new CourseUnitDto(idMap.get(LESSON_4), LESSON_4, ACTIVE, null, Constants.LESSON),
                                        new CourseUnitDto(idMap.get(LESSON_5), LESSON_5, ACTIVE, null, Constants.LESSON)
                                ),
                                new CourseUnitDto(idMap.get(QUIZ_5), QUIZ_5, ACTIVE, null, Constants.QUIZ))
                ), Constants.COURSE));
        coursesToUpdate.add(new CourseUnitDto(idMap.get(COURSE_3), COURSE_3, ACTIVE,
                asList(
                        new ChapterUnitDto(newChapter.getId(), "new_chapter", ACTIVE,
                                asList(
                                        new CourseUnitDto(idMap.get(LESSON_6), LESSON_6, ACTIVE, null, Constants.LESSON)
                                ), null)
                ), Constants.COURSE));

        getLogger().info("Updating course structure");
        courseStructureService.updateCourseStructure(coursesToUpdate);

        Course course = courseDataService.findById(idMap.get(COURSE_1));
        assertNotNull(course);
        assertEquals(2, course.getChapters().size());
        assertEquals(asList(CHAPTER_1, CHAPTER_2), extract(course.getChapters(), on(Chapter.class).getName()));
        verifyLessons(course.getChapters().get(0), 2, asList(LESSON_1, LESSON_2), asList(CourseUnitState.Active, CourseUnitState.Active));
        verifyQuiz(course.getChapters().get(0), QUIZ_2, CourseUnitState.Active);
        verifyLessons(course.getChapters().get(1), 1, asList(LESSON_3), asList(CourseUnitState.Active));
        verifyQuiz(course.getChapters().get(1), QUIZ_1, CourseUnitState.Active);

        course = courseDataService.findById(idMap.get(COURSE_2));
        assertNotNull(course);
        assertEquals(1, course.getChapters().size());
        verifyLessons(course.getChapters().get(0), 2, asList(LESSON_4, LESSON_5), asList(CourseUnitState.Active, CourseUnitState.Active));
        verifyQuiz(course.getChapters().get(0), QUIZ_5, CourseUnitState.Active);


        course = courseDataService.findById(idMap.get(COURSE_3));
        assertNotNull(course);
        assertEquals(1, course.getChapters().size());
        assertEquals("new_chapter", course.getChapters().get(0).getName());
        verifyLessons(course.getChapters().get(0), 1, asList(LESSON_6), asList(CourseUnitState.Active));
        assertNull(course.getChapters().get(0).getQuiz());

        // verify unused units
        verifyUnused(asList(QUIZ_3, QUIZ_4, QUIZ_6, "quiz_7"), asList("lesson_10", LESSON_7, LESSON_8, LESSON_9), asList(CHAPTER_4, CHAPTER_5, CHAPTER_6));
    }

    private void verifyUnused(List<String> quizzes, List<String> lessons, List<String> chapters){
        List<Quiz> allQuizzes = mTrainingService.getAllQuizzes();
        List<Lesson> allLessons = mTrainingService.getAllLessons();
        List<Chapter> allChapters = mTrainingService.getAllChapters();

        for(Quiz quiz : allQuizzes) {
            if(quizzes.contains(quiz.getName())) {
                assertNull(quiz.getChapter());
            }
        }
        for(Lesson lesson : allLessons) {
            if(lessons.contains(lesson.getName())) {
                assertNull(lesson.getChapter());
            }
        }
        for(Chapter chapter : allChapters) {
            if(chapters.contains(chapter.getName())) {
                assertNull(chapter.getCourse());
            }
        }
    }

    private void verifyLessons(Chapter chapter, int size, List<String> expectedLessons, List<CourseUnitState> expectedStates) {
        assertEquals(size, chapter.getLessons().size());
        List<String> lessons = extract(chapter.getLessons(), on(Lesson.class).getName());
        assertEquals(expectedLessons, lessons);
        List<CourseUnitState> states = extract(chapter.getLessons(), on(Lesson.class).getState());
        assertEquals(expectedStates, states);
    }

    private void verifyQuiz(Chapter chapter, String name, CourseUnitState state) {
        assertNotNull(chapter.getQuiz());
        assertEquals(name, chapter.getQuiz().getName());
        assertEquals(state, chapter.getQuiz().getState());
    }

    private void setUpData() {
        idMap = new HashMap<>();

        Map<String, String> props = new HashMap<>();
        props.put("key_1", "value_1");
        Quiz q1 = new Quiz(QUIZ_1, CourseUnitState.Active, "content_1", "Description_1", props);
        Quiz q2 = new Quiz(QUIZ_2, CourseUnitState.Active, "content_2", "Description_2", props);
        Quiz q3 = new Quiz(QUIZ_3, CourseUnitState.Active, "content_3", "Description_3", props);

        q1 = quizDataService.detachedCopy(quizDataService.create(q1));
        q2 = quizDataService.detachedCopy(quizDataService.create(q2));
        q3 = quizDataService.detachedCopy(quizDataService.create(q3));

        idMap.put(q1.getName(), q1.getId());
        idMap.put(q2.getName(), q2.getId());
        idMap.put(q3.getName(), q3.getId());

        Lesson l1 = new Lesson(LESSON_1, CourseUnitState.Active, "content_1", "Description_1", props);
        Lesson l2 = new Lesson(LESSON_2, CourseUnitState.Active, "content_2", "Description_2", props);
        Lesson l3 = new Lesson(LESSON_3, CourseUnitState.Active, "content_3", "Description_3", props);
        Lesson l4 = new Lesson(LESSON_4, CourseUnitState.Active, "content_4", "Description_4", props);
        Lesson l5 = new Lesson(LESSON_5, CourseUnitState.Active, "content_5", "Description_5", props);

        l1 = lessonDataService.detachedCopy(lessonDataService.create(l1));
        l2 = lessonDataService.detachedCopy(lessonDataService.create(l2));
        l3 = lessonDataService.detachedCopy(lessonDataService.create(l3));
        l4 = lessonDataService.detachedCopy(lessonDataService.create(l4));
        l5 = lessonDataService.detachedCopy(lessonDataService.create(l5));

        idMap.put(l1.getName(), l1.getId());
        idMap.put(l2.getName(), l2.getId());
        idMap.put(l3.getName(), l3.getId());
        idMap.put(l4.getName(), l4.getId());
        idMap.put(l5.getName(), l5.getId());

        Chapter ch1 = new Chapter(CHAPTER_1, CourseUnitState.Active, "content_1", "Description_1", props);
        Chapter ch2 = new Chapter(CHAPTER_2, CourseUnitState.Active, "content_2", "Description_2", props);
        Chapter ch3 = new Chapter(CHAPTER_3, CourseUnitState.Active, "content_3", "Description_3", props);

        ch1 = chapterDataService.detachedCopy(chapterDataService.create(ch1));
        ch2 = chapterDataService.detachedCopy(chapterDataService.create(ch2));
        ch3 = chapterDataService.detachedCopy(chapterDataService.create(ch3));

        idMap.put(ch1.getName(), ch1.getId());
        idMap.put(ch2.getName(), ch2.getId());
        idMap.put(ch3.getName(), ch3.getId());

        ch1.setQuiz(q1);
        ch2.setQuiz(q2);
        ch3.setQuiz(q3);

        List<Lesson> lessons1 = new ArrayList<>();
        List<Lesson> lessons2 = new ArrayList<>();
        List<Lesson> lessons3 = new ArrayList<>();

        lessons1.add(l1);
        lessons1.add(l2);
        lessons2.add(l3);
        lessons3.add(l4);
        lessons3.add(l5);

        ch1.setLessons(lessons1);
        ch2.setLessons(lessons2);
        ch3.setLessons(lessons3);
        ch1 = chapterDataService.detachedCopy(chapterDataService.update(ch1));
        ch2 = chapterDataService.detachedCopy(chapterDataService.update(ch2));
        ch3 = chapterDataService.detachedCopy(chapterDataService.update(ch3));

        Course c1 = new Course(COURSE_1, CourseUnitState.Active, "content_1", "Description_1", props);
        Course c2 = new Course(COURSE_2, CourseUnitState.Active, "content_2", "Description_2", props);

        c1 = courseDataService.detachedCopy(courseDataService.create(c1));
        c2 = courseDataService.detachedCopy(courseDataService.create(c2));

        idMap.put(c1.getName(), c1.getId());
        idMap.put(c2.getName(), c2.getId());

        List<Chapter> chapters1 = new ArrayList<>();
        List<Chapter> chapters2 = new ArrayList<>();

        chapters1.add(ch1);
        chapters1.add(ch2);
        chapters2.add(ch3);

        c1.setChapters(chapters1);
        c2.setChapters(chapters2);
        courseDataService.update(c1);
        courseDataService.update(c2);

        //unused elements
        Lesson l6 = new Lesson(LESSON_6, CourseUnitState.Active, "content_6", "Description_6", props);
        Lesson l7 = new Lesson(LESSON_7, CourseUnitState.Active, "content_7", "Description_7", props);
        Lesson l8 = new Lesson(LESSON_8, CourseUnitState.Active, "content_8", "Description_8", props);
        Lesson l9 = new Lesson(LESSON_9, CourseUnitState.Active, "content_9", "Description_9", props);
        l6 = lessonDataService.detachedCopy(lessonDataService.create(l6));
        l7 = lessonDataService.detachedCopy(lessonDataService.create(l7));
        l8 = lessonDataService.detachedCopy(lessonDataService.create(l8));
        l9 = lessonDataService.detachedCopy(lessonDataService.create(l9));

        idMap.put(l6.getName(), l6.getId());
        idMap.put(l7.getName(), l7.getId());
        idMap.put(l8.getName(), l8.getId());
        idMap.put(l9.getName(), l9.getId());

        Chapter ch4 = new Chapter(CHAPTER_4, CourseUnitState.Active, "content_4", "Description_4", props);
        Chapter ch5 = new Chapter(CHAPTER_5, CourseUnitState.Active, "content_5", "Description_5", props);
        Chapter ch6 = new Chapter(CHAPTER_6, CourseUnitState.Active, "content_6", "Description_6", props);
        ch4 = chapterDataService.detachedCopy(chapterDataService.create(ch4));
        ch5 = chapterDataService.detachedCopy(chapterDataService.create(ch5));
        ch6 = chapterDataService.detachedCopy(chapterDataService.create(ch6));

        idMap.put(ch4.getName(), ch4.getId());
        idMap.put(ch5.getName(), ch5.getId());
        idMap.put(ch6.getName(), ch6.getId());

        Quiz q4 = new Quiz(QUIZ_4, CourseUnitState.Active, "content_4", "Description_4", props);
        Quiz q5 = new Quiz(QUIZ_5, CourseUnitState.Active, "content_5", "Description_5", props);
        Quiz q6 = new Quiz(QUIZ_6, CourseUnitState.Active, "content_6", "Description_6", props);
        q4 = quizDataService.detachedCopy(quizDataService.create(q4));
        q5 = quizDataService.detachedCopy(quizDataService.create(q5));
        q6 = quizDataService.detachedCopy(quizDataService.create(q6));

        idMap.put(q4.getName(), q4.getId());
        idMap.put(q5.getName(), q5.getId());
        idMap.put(q6.getName(), q6.getId());

        Course c3 = new Course(COURSE_3, CourseUnitState.Active, "content_3", "Description_3", props);
        Course c4 = new Course(COURSE_4, CourseUnitState.Active, "content_4", "Description_4", props);
        c3 = courseDataService.detachedCopy(courseDataService.create(c3));
        c4 = courseDataService.detachedCopy(courseDataService.create(c4));

        idMap.put(c3.getName(), c3.getId());
        idMap.put(c4.getName(), c4.getId());
    }

}