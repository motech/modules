package org.motechproject.mtraining.domain;


/**
 * Domain model to represent course structure
 */
public class Course {

    private String name;

    public Course(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
