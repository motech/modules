package org.motechproject.mtraining.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

/**
 * Lesson domain object forms the leaf node in the course structure hierarchy.
 */
@Entity
public class Lesson extends CourseUnitMetadata {

    public Lesson() {
        this(null);
    }

    public Lesson(String content) {
        setContent(content);
    }

}
