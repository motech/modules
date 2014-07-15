package org.motechproject.mtraining.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.util.List;

/**
 * Chapter object to store quiz and lesson metadata
 */
@Entity
public class Chapter extends CourseUnitMetadata {

    @Field
    private List<Lesson> lessons;

    @Field
    private Quiz quiz;

    public Chapter() {
        this(null, CourseUnitState.Inactive, null);
    }

    public Chapter(String name, CourseUnitState state, String content) {

        this(name, state, content, null, null);
    }

    public Chapter(String name, CourseUnitState state, String content, List<Lesson> lessons) {

        this(name, state, content, lessons, null);
    }

    public Chapter(String name, CourseUnitState state, String content, List<Lesson> lessons, Quiz quiz) {
        super(name, state, content);
        this.lessons = lessons;
        this.quiz = quiz;
    }

    public List<Lesson> getLessons() {

        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {

        this.lessons = lessons;
    }

    public Quiz getQuiz() {

        return quiz;
    }

    public void setQuiz(Quiz quiz) {

        this.quiz = quiz;
    }
}
