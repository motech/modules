package org.motechproject.mtraining.domain;

import org.motechproject.mds.annotations.Entity;

/**
 * Lesson domain object forms the leaf node in the course structure hierarchy.
 */
@Entity
public class Lesson extends CourseUnitMetadata {

    public Lesson(String name, boolean status, String content) {

        super(name, status, content);
    }

}
