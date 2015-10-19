package org.motechproject.mtraining.service.impl;

import org.motechproject.mtraining.domain.Chapter;
import org.motechproject.mtraining.domain.Course;
import org.motechproject.mtraining.domain.CourseUnitState;
import org.motechproject.mtraining.domain.Lesson;
import org.motechproject.mtraining.domain.Quiz;
import org.motechproject.mtraining.dto.ChapterUnitDto;
import org.motechproject.mtraining.dto.CourseUnitDto;
import org.motechproject.mtraining.exception.CourseUnitNotFoundException;
import org.motechproject.mtraining.repository.ChapterDataService;
import org.motechproject.mtraining.repository.CourseDataService;
import org.motechproject.mtraining.repository.LessonDataService;
import org.motechproject.mtraining.repository.QuizDataService;
import org.motechproject.mtraining.service.CourseStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementation of the {@link org.motechproject.mtraining.service.CourseStructureService}.
 * This service allows creating relations between course units(Course, Chapter, Quiz and Lesson).
 */
@Service("courseStructureService")
public class CourseStructureServiceImpl implements CourseStructureService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseStructureServiceImpl.class);

    @Autowired
    private CourseDataService courseDataService;

    @Autowired
    private ChapterDataService chapterDataService;

    @Autowired
    private LessonDataService lessonDataService;

    @Autowired
    private QuizDataService quizDataService;

    @Override
    @Transactional
    public void updateCourseStructure(List<CourseUnitDto> courses) {
        Set<Long> unusedQuizzes = new HashSet<>();
        Set<Long> unusedLessons = new HashSet<>();

        List<Course> coursesFromDb = fillUnusedUnits(courses, unusedLessons, unusedQuizzes);

        int i = 0;
        for (CourseUnitDto courseUnitDto : courses) {
            LOGGER.debug("Start processing course with id {} and name {}", courseUnitDto.getId(), courseUnitDto.getName());

            Course course = coursesFromDb.get(i);
            List<Chapter> chapters = new ArrayList<>();

            if (courseUnitDto.getUnits() != null) {
                for (CourseUnitDto chapterUnitDto : courseUnitDto.getUnits()) {
                    Chapter chapter = chapterDataService.findChapterById(chapterUnitDto.getId());

                    if (chapter == null) {
                        throw new CourseUnitNotFoundException("Cannot find chapter with id: " + chapterUnitDto.getId());
                    }

                    setUpLessons(chapterUnitDto, chapter, unusedLessons);
                    setUpQuiz((ChapterUnitDto) chapterUnitDto, chapter, unusedQuizzes);
                    chapter.setState(CourseUnitState.valueOf(chapterUnitDto.getState()));
                    chapters.add(chapterDataService.update(chapter));
                }
            }

            course.setState(CourseUnitState.valueOf(courseUnitDto.getState()));
            course.setChapters(chapters);
            courseDataService.update(course);
            i++;
        }

        clearRelationsInUnusedUnits(unusedLessons, unusedQuizzes);
    }

    private void setUpLessons(CourseUnitDto chapterDto, Chapter chapter, Set<Long> unusedLessons) {
        List<Lesson> lessons = new ArrayList<>();

        if (chapterDto.getUnits() != null) {
            for (CourseUnitDto lessonUnitDto : chapterDto.getUnits()) {
                Lesson lesson = lessonDataService.findLessonById(lessonUnitDto.getId());
                if (lesson == null) {
                    throw new CourseUnitNotFoundException("Cannot find lesson with id: " + lessonUnitDto.getId());
                }

                lesson.setState(CourseUnitState.valueOf(lessonUnitDto.getState()));
                lessons.add(lesson);
                unusedLessons.remove(lesson.getId());
            }
        }

        chapter.setLessons(lessons);
    }

    private void setUpQuiz(ChapterUnitDto chapterDto, Chapter chapter, Set<Long> unusedQuizzes) {
        if (chapterDto.getQuiz() != null) {
            Quiz quiz = quizDataService.findQuizById(chapterDto.getQuiz().getId());
            if (quiz == null) {
                throw new CourseUnitNotFoundException("Cannot find quiz with id: " + chapterDto.getQuiz().getId());
            }

            quiz.setState(CourseUnitState.valueOf(chapterDto.getQuiz().getState()));
            chapter.setQuiz(quiz);
            unusedQuizzes.remove(quiz.getId());
        } else {
            chapter.setQuiz(null);
        }
    }

    private List<Course> fillUnusedUnits(List<CourseUnitDto> courses, Set<Long> unusedLessons, Set<Long> unusedQuizzes) {
        List<Course> coursesFromDb = new ArrayList<>();

        for (CourseUnitDto courseDto : courses) {
            Course course = courseDataService.findById(courseDto.getId());
            if (course == null) {
                throw new CourseUnitNotFoundException("Cannot find course with id: " + courseDto.getId());
            }

            coursesFromDb.add(course);
            List<Chapter> chapters = course.getChapters();

            if (chapters != null) {
                for (Chapter chapter : chapters) {

                    Quiz oldQuiz = chapter.getQuiz();
                    if (oldQuiz != null) {
                        unusedQuizzes.add(oldQuiz.getId());
                    }

                    List<Lesson> lessons = chapter.getLessons();
                    if (lessons != null) {
                        for (Lesson oldLesson : chapter.getLessons()) {
                            unusedLessons.add(oldLesson.getId());
                        }
                    }
                }
            }
        }
        return coursesFromDb;
    }

    private void clearRelationsInUnusedUnits(Set<Long> unusedLessons, Set<Long> unusedQuizzes) {
        List<Lesson> lessonsToUpdate = new ArrayList<>();

        if (unusedLessons.size() > 0) {
            lessonsToUpdate = lessonDataService.findLessonsByIds(unusedLessons);
            if (lessonsToUpdate.size() != unusedLessons.size()) {
                throw new CourseUnitNotFoundException("Cannot find lesson with id");
            }
        }

        List<Quiz> quizzesToUpdate = new ArrayList<>();
        if (unusedLessons.size() > 0) {
            quizzesToUpdate = quizDataService.findQuizzesByIds(unusedQuizzes);
            if (quizzesToUpdate.size() != unusedQuizzes.size()) {
                throw new CourseUnitNotFoundException("Cannot find quiz with id");
            }
        }

        for (Lesson unusuedLesson : lessonsToUpdate) {
            unusuedLesson.setChapter(null);
            lessonDataService.update(unusuedLesson);
            LOGGER.debug("Lesson with id: {} marked as unused", unusuedLesson.getId());
        }

        for (Quiz unusedQuiz : quizzesToUpdate) {
            unusedQuiz.setChapter(null);
            quizDataService.update(unusedQuiz);
            LOGGER.debug("Quiz with id: {} marked as unused", unusedQuiz.getId());
        }
    }
}
