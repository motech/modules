package org.motechproject.mtraining.domain;

/**
 * Common metadata shared by all course sub classes
 */
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
