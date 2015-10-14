package org.motechproject.mtraining.web;

import org.motechproject.mtraining.domain.Chapter;
import org.motechproject.mtraining.domain.Course;
import org.motechproject.mtraining.domain.Lesson;
import org.motechproject.mtraining.domain.Quiz;
import org.motechproject.mtraining.dto.ChapterUnitDto;
import org.motechproject.mtraining.dto.CourseUnitDto;
import org.motechproject.mtraining.dto.CourseUnitListWrapper;
import org.motechproject.mtraining.service.MTrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides API for the treeView page for managing courses, chapters, quizzes and lessons.
 */
@Controller
public class TreeViewController {

    @Autowired
    private MTrainingService mTrainingService;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/courses", method = RequestMethod.GET)
    @ResponseBody
    public List<CourseUnitDto> getAllCourses() {
        List<CourseUnitDto> courses = new ArrayList<>();
        for (Course course : mTrainingService.getAllCourses()) {
            courses.add(course.toUnitDto());
        }
        return courses;
    }

    @RequestMapping("/chapters")
    @ResponseBody
    public List<ChapterUnitDto> getUnusedChapters() {
        List<ChapterUnitDto> chapters = new ArrayList<>();
        for (Chapter chapter : mTrainingService.getAllChapters()) {
            chapters.add((ChapterUnitDto) chapter.toUnitDto());
        }
        return chapters;
    }

    @RequestMapping("/lessons")
    @ResponseBody
    public List<CourseUnitDto> getUnusedLessons() {
        List<CourseUnitDto> lessons = new ArrayList<>();
        for (Lesson lesson: mTrainingService.getAllLessons()) {
            lessons.add(lesson.toUnitDto());
        }
        return lessons;
    }

    @RequestMapping("/quizzes")
    @ResponseBody
    public List<CourseUnitDto> getUnusedQuizzes() {
        List<CourseUnitDto> quizzes = new ArrayList<>();
        for (Quiz quiz: mTrainingService.getAllQuizzes()) {
            quizzes.add(quiz.toUnitDto());
        }
        return quizzes;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/updateCourses", method = RequestMethod.POST)
    public void updateRelations(@RequestBody CourseUnitListWrapper courses) throws Exception {
        //TODO saving
    }
}
