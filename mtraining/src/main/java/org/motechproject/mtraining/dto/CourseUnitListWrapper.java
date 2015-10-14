package org.motechproject.mtraining.dto;

import java.util.List;

/**
 * Wrapper class for list of course units.
 */
public class CourseUnitListWrapper {

    /**
     * The list of the course units.
     */
    private List<CourseUnitDto> courses;

    public List<CourseUnitDto> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseUnitDto> courses) {
        this.courses = courses;
    }
}