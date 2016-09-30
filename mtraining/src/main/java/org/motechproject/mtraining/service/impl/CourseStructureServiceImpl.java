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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public void updateCourseStructure(List<CourseUnitDto> courses) {
        // maps to store objects from old course structure
        Map<Long, Course> courseMap = new HashMap<>();
        Map<Long, Chapter> chapterMap = new HashMap<>();
        Map<Long, Lesson> lessonMap = new HashMap<>();
        Map<Long, Quiz> quizMap = new HashMap<>();

        //we clear old relations and fill maps with objects to update
        clearRelationships(courses, courseMap, chapterMap, lessonMap, quizMap);

        for (CourseUnitDto courseUnitDto : courses) {
            LOGGER.debug("Start processing course with id {} and name {}", courseUnitDto.getId(), courseUnitDto.getName());

            Course course = courseMap.get(courseUnitDto.getId());

            if (course == null) {
                throw new CourseUnitNotFoundException("Cannot find course with id: " + courseUnitDto.getId());
            }

            List<Chapter> chapters = new ArrayList<>();
            if (courseUnitDto.getUnits() != null) {
                for (CourseUnitDto chapterUnitDto : courseUnitDto.getUnits()) {
                    Chapter chapter = getChapter(chapterUnitDto.getId(), chapterMap);

                    setUpLessons(chapterUnitDto, chapter, lessonMap);
                    setUpQuiz((ChapterUnitDto) chapterUnitDto, chapter, quizMap);

                    chapter.setCourse(course);
                    chapter.setState(CourseUnitState.valueOf(chapterUnitDto.getState()));
                    chapters.add(chapter);
                    chapterMap.remove(chapterUnitDto.getId());
                }
            }

            course.setState(CourseUnitState.valueOf(courseUnitDto.getState()));
            course.setChapters(chapters);
            courseMap.remove(courseUnitDto.getId());
            courseDataService.update(course);
        }
        // we must evict cache to avoid relations inconsistent
        courseDataService.evictEntityCache(true);
        updateUnusedRecords(courseMap, chapterMap, lessonMap, quizMap);
    }

    private void updateUnusedRecords(Map<Long, Course> courseMap, Map<Long, Chapter> chapterMap, Map<Long, Lesson> lessonMap, Map<Long, Quiz> quizMap) {
        for (Long courseId : courseMap.keySet()) {
            courseDataService.update(courseMap.get(courseId));
        }
        for (Long courseId : chapterMap.keySet()) {
            chapterDataService.update(chapterMap.get(courseId));
        }
        for (Long courseId : lessonMap.keySet()) {
            lessonDataService.update(lessonMap.get(courseId));
        }
        for (Long courseId : quizMap.keySet()) {
            quizDataService.update(quizMap.get(courseId));
        }
    }

    private void setUpLessons(CourseUnitDto chapterDto, Chapter chapter, Map<Long, Lesson> lessonMap) {
        List<Lesson> lessons = new ArrayList<>();

        if (chapterDto.getUnits() != null) {
            for (CourseUnitDto lessonUnitDto : chapterDto.getUnits()) {
                Lesson lesson = getLesson(lessonUnitDto.getId(), lessonMap);
                lesson.setState(CourseUnitState.valueOf(lessonUnitDto.getState()));
                lessons.add(lesson);
                lesson.setChapter(chapter);
                lessonMap.remove(lessonUnitDto.getId());
            }
        }
        chapter.setLessons(lessons);
    }

    private void setUpQuiz(ChapterUnitDto chapterDto, Chapter chapter, Map<Long, Quiz> quizMap) {
        if (chapterDto.getQuiz() != null) {
            Quiz quiz = getQuiz(chapterDto.getQuiz().getId(), quizMap);
            quiz.setState(CourseUnitState.valueOf(chapterDto.getQuiz().getState()));
            chapter.setQuiz(quiz);
            quiz.setChapter(chapter);
            quizMap.remove(chapterDto.getQuiz().getId());
        }
    }

    private Chapter getChapter(Long id, Map<Long, Chapter> chapterMap) {
        Chapter chapter = chapterMap.get(id);

        if (chapter == null) {
            chapter = chapterDataService.findChapterById(id);
            //we must check if it has child nodes and disconnect them.
            if (chapter != null) {
                Quiz oldQuiz = chapter.getQuiz();
                List<Lesson> oldLessons = chapter.getLessons();
                chapter.setQuiz(null);
                chapter.setLessons(null);

                if (oldQuiz != null) {
                    oldQuiz.setChapter(null);
                    quizDataService.update(oldQuiz);
                    LOGGER.debug("Quiz wit id: {}, was disconnected from chapter with id: {}", oldQuiz.getId(), chapter.getName());
                }

                for (Lesson oldLesson : oldLessons) {
                    oldLesson.setChapter(null);
                    lessonDataService.update(oldLesson);
                    LOGGER.debug("Lesson wit id: {}, was disconnected from chapter with id: {}", oldQuiz.getId(), chapter.getName());
                }
            } else {
                throw new CourseUnitNotFoundException("Cannot find chapter with id: " + id);
            }
        }

        return chapter;
    }


    private Lesson getLesson(Long id, Map<Long, Lesson> lessonMap) {
        Lesson lesson = lessonMap.get(id);

        if (lesson == null) {
            lesson = lessonDataService.findLessonById(id);
            if (lesson == null) {
                throw new CourseUnitNotFoundException("Cannot find lesson with id: " + id);
            }
        }

        return lesson;
    }

    private Quiz getQuiz(Long id, Map<Long, Quiz> quizMap) {
        Quiz quiz = quizMap.get(id);

        if (quiz == null) {
            quiz = quizDataService.findQuizById(id);
            if (quiz == null) {
                throw new CourseUnitNotFoundException("Cannot find quiz with id: " + id);
            }
        }

        return quiz;
    }

    private void clearRelationships(List<CourseUnitDto> courses, Map<Long, Course> courseMap, Map<Long, Chapter> chapterMap, Map<Long, Lesson> lessonMap, Map<Long, Quiz> quizMap) {
        for (CourseUnitDto courseDto : courses) {
            Course course = courseDataService.detachedCopy(courseDataService.findCourseById(courseDto.getId()));

            if (course == null) {
                throw new CourseUnitNotFoundException("Cannot find course with id: " + courseDto.getId());
            }

            courseMap.put(course.getId(), course);
            List<Chapter> chapters = course.getChapters();
            course.setChapters(null);

            if (chapters != null) {
                for (Chapter chapter : chapters) {
                    chapterMap.put(chapter.getId(), chapter);

                    Quiz quiz = chapter.getQuiz();
                    if (quiz != null) {
                        quizMap.put(quiz.getId(), quiz);
                        quiz.setChapter(null);
                    }

                    List<Lesson> lessons = chapter.getLessons();
                    if (lessons != null) {
                        for (Lesson lesson : chapter.getLessons()) {
                            lessonMap.put(lesson.getId(), lesson);
                            lesson.setChapter(null);
                        }
                    }

                    chapter.setQuiz(null);
                    chapter.setCourse(null);
                    chapter.setLessons(null);
                }
            }
        }
    }
}
