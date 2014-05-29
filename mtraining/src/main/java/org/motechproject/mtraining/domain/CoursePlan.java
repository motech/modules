package org.motechproject.mtraining.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.util.List;

/**
 * Course plan contains a schedule(collection) or courses grouped by location
 */
@Entity
public class CoursePlan {

    @Field
    private String name;

    @Field
    private List<Course> courses;
}
