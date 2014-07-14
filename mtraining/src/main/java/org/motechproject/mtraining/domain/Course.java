package org.motechproject.mtraining.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.util.List;

/**
 * Course metadata containing chapter information
 */
@Entity
public class Course extends CourseUnitMetadata {

    @Field
    private List<Chapter> chapters;


    public Course(String name, boolean status, String content) {

        this(name, status, content, null);
    }

    public Course(String name, boolean status, String content, List<Chapter> chapters) {
        super(name, status, content);
        this.chapters = chapters;
    }

    public List<Chapter> getChapters() {

        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {

        this.chapters = chapters;
    }
}
