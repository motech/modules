package org.motechproject.mtraining.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;
import org.motechproject.mtraining.dto.CourseUnitDto;

import java.util.Objects;

/**
 * Common metadata shared by all course sub classes.
 */
@Entity
public abstract class CourseUnitMetadata extends MdsEntity {

    /**
     * Name of the course unit
     */
    @Field
    private String name;

    /**
     * Status of the course unit
     */
    @Field(required = true)
    private CourseUnitState state;

    /**
     * The content for the course unit. This could be url or string representation for
     * where to find the content in the external system. For example, this could be a
     * resource pointer for an audio file in an IVR system.
     */
    @Field
    private String content;

    /**
     * Description of the unit.
     */
    @Field
    private String description;

    /**
     * Constructor with all arguments.
     *
     * @param name Name of the unit
     * @param state Status of the unit
     * @param content Content reference for the unit
     * @param description the description for the unit
     */
    public CourseUnitMetadata(String name, CourseUnitState state, String content, String description) {
        this.name = name;
        this.state = state;
        this.content = content;
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CourseUnitState getState() {
        return state;
    }

    public void setState(CourseUnitState state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Builds the dto representation of the unit used by the tree view.
     *
     * @return the dto representation of the unit
     */
    public CourseUnitDto toUnitDto() {
        return new CourseUnitDto(getId(), getName(), getState().toString(), null, null);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final CourseUnitMetadata other = (CourseUnitMetadata) obj;

        return Objects.equals(this.getId(), other.getId())
                && Objects.equals(this.getName(), other.getName())
                && Objects.equals(this.getContent(), other.getContent())
                && Objects.equals(this.getDescription(), other.getDescription())
                && Objects.equals(this.getState(), other.getState());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getContent(), getDescription(), getState());
    }
}
