package org.motechproject.mtraining.domain;

import org.codehaus.jackson.annotate.JsonManagedReference;
import org.motechproject.mds.annotations.Access;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.util.SecurityMode;
import org.motechproject.mtraining.dto.CourseUnitDto;
import org.motechproject.mtraining.util.Constants;

import javax.jdo.annotations.Persistent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Course metadata containing chapter information
 * Structure -
 * Course is a collection of chapters
 * Chapter is a collection of lessons
 * Every Chapter has a quiz associated with it
 * Quiz is a collection of questions
 * Question contains pointer to question resource and answer resource.
 */
@Entity(maxFetchDepth = 3)
@Access(value = SecurityMode.PERMISSIONS, members = {Constants.MANAGE_MTRAINING})
public class Course extends CourseUnitMetadata {

    /**
     * List of chapters in the Course.
     */
    @Field
    @Persistent(defaultFetchGroup = Constants.TRUE,  mappedBy = "course")
    @JsonManagedReference
    private List<Chapter> chapters;

    /**
     * The additional properties which can be used with the Course.
     */
    @Field
    @Persistent(defaultFetchGroup = Constants.TRUE)
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
        if (chapters == null) {
            return new ArrayList<>();
        }
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

    /**
     * {@inheritDoc}
     */
    @Override
    public CourseUnitDto toUnitDto() {
        List<CourseUnitDto> units = new ArrayList<>();
        if (chapters != null) {
            for (Chapter chapter : chapters) {
                units.add(chapter.toUnitDto());
            }
        }
        return new CourseUnitDto(getId(), getName(), getState().toString(), units, Constants.COURSE);
    }

    @Override // NO CPD
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final Course other = (Course) obj;

        return Objects.equals(this.getId(), other.getId())
                && Objects.equals(this.getName(), other.getName())
                && Objects.equals(this.getDescription(), other.getDescription())
                && Objects.equals(this.getProperties(), other.getProperties())
                && Objects.equals(this.getState(), other.getState())
                && Objects.equals(this.getContent(), other.getContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription(), getProperties(), getState(), getContent());
    }
}
