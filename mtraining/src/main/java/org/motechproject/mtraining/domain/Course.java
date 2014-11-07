package org.motechproject.mtraining.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.Persistent;
import java.util.List;

/**
 * Course metadata containing chapter information
 * Structure -
 * Course is a collection of chapters
 * Chapter is a collection of lessons
 * Every Chapter has a quiz associated with it
 * Quiz is a collection of questions
 * Question contains pointer to question resource and answer resource
 */
@Entity
public class Course extends CourseUnitMetadata {

    /**
     * List of chapters in the course
     */
    @Field
    @Persistent(defaultFetchGroup = "true")
    private List<Chapter> chapters;

    public Course(String name, CourseUnitState state, String content) {

        this(name, state, content, null);
    }

    public Course(String name, CourseUnitState state, String content, List<Chapter> chapters) {
        super(name, state, content);
        this.chapters = chapters;
    }

    public List<Chapter> getChapters() {

        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {

        this.chapters = chapters;
    }
}
