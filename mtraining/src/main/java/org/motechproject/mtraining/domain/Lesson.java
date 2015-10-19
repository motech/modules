package org.motechproject.mtraining.domain;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.motechproject.mds.annotations.Access;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.Ignore;
import org.motechproject.mds.util.SecurityMode;
import org.motechproject.mtraining.dto.CourseUnitDto;
import org.motechproject.mtraining.util.Constants;

import javax.jdo.annotations.Persistent;
import java.util.Map;

/**
 * Lesson domain object forms the leaf node in the course structure hierarchy. A lesson is typically the
 * leaf node in the Course structure hierarchy.
 */
@Entity(maxFetchDepth = 4)
@Access(value = SecurityMode.PERMISSIONS, members = {Constants.MANAGE_MTRAINING})
public class Lesson extends CourseUnitMetadata {

    /**
     * The additional properties which can be used with the Lesson.
     */
    @Field
    @Persistent(defaultFetchGroup = Constants.TRUE)
    private Map<String, String> properties;

    /**
     * Chapter that owns this Lesson.
     */
    @Field
    @JsonBackReference
    @Persistent(defaultFetchGroup = Constants.TRUE)
    private Chapter chapter;

    public Lesson() {
        this(null, CourseUnitState.Inactive, null, null, null);
    }

    public Lesson(String name, CourseUnitState state, String content, String description, Map<String, String> properties) {
        super(name, state, content, description);
        this.properties = properties;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }


    public Chapter getChapter() {
        return chapter;
    }

    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }

    /**
     * {@inheritDoc}
     */
    @Ignore
    @Override
    public CourseUnitDto toUnitDto() {
        return new CourseUnitDto(getId(), getName(), getState().toString(), null, Constants.LESSON);
    }
}
