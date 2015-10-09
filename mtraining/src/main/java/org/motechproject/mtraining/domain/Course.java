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
 * Course metadata containing chapter information
 * Structure -
 * Course is a collection of chapters
 * Chapter is a collection of lessons
 * Every Chapter has a quiz associated with it
 * Quiz is a collection of questions
 * Question contains pointer to question resource and answer resource.
 */
@Entity
@Access(value = SecurityMode.PERMISSIONS, members = {Constants.MANAGE_MTRAINING})
public class Course extends CourseUnitMetadata {

    /**
     * List of chapters in the Course.
     */
    @Field
    @Persistent(defaultFetchGroup = "true")
    private List<Chapter> chapters;

    /**
     * The additional properties which can be used with the Course.
     */
    @Field
    @Persistent(defaultFetchGroup = "true")
    private Map<String, String> properties;

    public Course() {
        this(null, CourseUnitState.Inactive, null, null, null);
    }

    public Course(String name, CourseUnitState state, String content, String description, Map<String, String> properties) {
        this(name, state, content, description, properties, null);
    }

    public Course(String name, CourseUnitState state, String content, String description, Map<String, String> properties, List<Chapter> chapters) {
        super(name, state, content, description);
        this.chapters = chapters;
        this.properties = properties;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
}
