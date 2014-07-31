package org.motechproject.mtraining.domain;

import org.motechproject.mds.annotations.Entity;

/**
 * Lesson domain object forms the leaf node in the course structure hierarchy. A lesson is typically the
 * leaf node in the Course structure hierarchy
 */
@Entity
public class Lesson extends CourseUnitMetadata {

    public Lesson(String name, CourseUnitState state, String content) {

        super(name, state, content);
    }

}
