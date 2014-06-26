package org.motechproject.mtraining.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.util.List;

/**
 * Created by kosh on 5/29/14.
 */
@Entity
public class Course extends CourseUnitMetadata {

    @Field
    private List<Chapter> chapters;

    public Course() {
        this(null);
    }

    public Course(List<Chapter> chapters) {
        this.chapters = chapters;
    }
    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setModules(List<Chapter> chapters) {
        this.chapters = chapters;
    }
}
