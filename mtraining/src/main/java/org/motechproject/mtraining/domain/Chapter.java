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

    public Chapter(String name, boolean status, String content) {
        this(name, status, content, null, null);
    }

    public Chapter(String name, boolean status, String content, List<Lesson> lessons) {
        this(name, status, content, lessons, null);
    }

    public Chapter(String name, boolean status, String content, List<Lesson> lessons, Quiz quiz) {
        super(name, status, content);
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
