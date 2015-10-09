package org.motechproject.mtraining.domain;

import org.motechproject.mds.annotations.Access;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.util.SecurityMode;
import org.motechproject.mtraining.util.Constants;

import javax.jdo.annotations.Persistent;
import java.util.List;
import java.util.Map;

/**
 * Chapter object to store quiz and lesson metadata. A chapter contains a list of possible lessons.
 */
@Entity
@Access(value = SecurityMode.PERMISSIONS, members = {Constants.MANAGE_MTRAINING})
public class Chapter extends CourseUnitMetadata {

    /**
     * List of lessons in the Chapter.
     */
    @Field
    private List<Lesson> lessons;

    /**
     * Quiz for the Chapter.
     */
    @Field
    @Persistent(defaultFetchGroup = "true")
    private Quiz quiz;

    /**
     * The additional properties which can be used with the Chapter.
     */
    @Field
    @Persistent(defaultFetchGroup = "true")
    private Map<String, String> properties;

    public Chapter() {
        this(null, CourseUnitState.Inactive, null, null, null);
    }

    public Chapter(String name, CourseUnitState state, String content, String description, Map<String, String> properties) {
        this(name, state, content, description, properties, null, null);
    }

    public Chapter(String name, CourseUnitState state, String content, String description,
                   Map<String, String> properties, List<Lesson> lessons) {
        this(name, state, content, description, properties, lessons, null);
    }

    public Chapter(String name, CourseUnitState state, String content, String description,
                   Map<String, String> properties, List<Lesson> lessons, Quiz quiz) {
        super(name, state, content, description);
        this.lessons = lessons;
        this.quiz = quiz;
        this.properties = properties;
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

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
}
