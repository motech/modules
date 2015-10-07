package org.motechproject.mtraining.domain;

import org.motechproject.mds.annotations.Access;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.util.SecurityMode;
import org.motechproject.mtraining.util.Constants;

import javax.jdo.annotations.Persistent;
import java.util.List;

/**
 * Chapter object to store quiz and lesson metadata. A chapter contains a list of possible lessons
 */
@Entity
@Access(value = SecurityMode.PERMISSIONS, members = {Constants.MANAGE_MTRAINING})
public class Chapter extends CourseUnitMetadata {

    /**
     * List of lessons in the Chapter
     */
    @Field
    private List<Lesson> lessons;

    /**
     * Quiz for the chapter
     */
    @Field
    @Persistent(defaultFetchGroup = "true")
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

        return this.quiz;
    }

    public void setQuiz(Quiz quiz) {

        this.quiz = quiz;
    }
}
