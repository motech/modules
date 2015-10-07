package org.motechproject.mtraining.domain;

import org.motechproject.mds.annotations.Access;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.util.SecurityMode;
import org.motechproject.mtraining.util.Constants;

/**
 * Lesson domain object forms the leaf node in the course structure hierarchy. A lesson is typically the
 * leaf node in the Course structure hierarchy
 */
@Entity
@Access(value = SecurityMode.PERMISSIONS, members = {Constants.MANAGE_MTRAINING})
public class Lesson extends CourseUnitMetadata {

    public Lesson(String name, CourseUnitState state, String content) {

        super(name, state, content);
    }

}
