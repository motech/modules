package org.motechproject.mtraining.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

/**
 * Lesson domain object forms the leaf node in the course structure hierarchy.
 */
@Entity
public class Lesson extends CourseUnitMetadata {

    @Field
    private String content;

    public Lesson() {
        this(null);
    }

    public Lesson(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
