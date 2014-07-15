package org.motechproject.mtraining.domain;

import org.motechproject.mds.annotations.Entity;

/**
 * Common metadata shared by all course sub classes
 */
@Entity
public class CourseUnitMetadata {

    /**
     * Name of the course unit
     */
    private String name;

    /**
     * Status of the course unit
     */
    private boolean status;

    /**
     * The content for the course unit
     */
    private String content;

    /**
     * Constructor with 0 arguments
     */
    public CourseUnitMetadata() {
        this.name = "";
        this.status = false;
        this.content = "";
    }

    /**
     * Constructor with all arguments
     * @param name Name of the unit
     * @param status Status of the unit
     * @param content Content reference for the unit
     */
    public CourseUnitMetadata(String name, boolean status, String content) {
        this.name = name;
        this.status = status;
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
