package org.motechproject.mtraining.domain;

import org.motechproject.mds.annotations.Entity;

/**
 * Common metadata shared by all course sub classes
 */
@Entity
public class CourseUnitMetadata extends MdsEntity {

    /**
     * Name of the course unit
     */
    private String name;

    /**
     * Status of the course unit
     */
    private CourseUnitState state;

    /**
     * The content for the course unit
     */
    private String content;

    /**
     * Constructor with 0 arguments
     */
    public CourseUnitMetadata() {
        this.name = "";
        this.state = CourseUnitState.Inactive;
        this.content = "";
    }

    /**
     * Constructor with all arguments
     * @param name Name of the unit
     * @param state Status of the unit
     * @param content Content reference for the unit
     */
    public CourseUnitMetadata(String name, CourseUnitState state, String content) {
        this.name = name;
        this.state = state;
        this.content = content;
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
}
