package org.motechproject.mtraining.domain;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonManagedReference;
import org.motechproject.mds.annotations.Access;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.Ignore;
import org.motechproject.mds.util.SecurityMode;
import org.motechproject.mtraining.dto.ChapterUnitDto;
import org.motechproject.mtraining.dto.CourseUnitDto;
import org.motechproject.mtraining.util.Constants;

import javax.jdo.annotations.Persistent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Chapter object to store quiz and lesson metadata. A chapter contains a list of possible lessons.
 */
@Entity(maxFetchDepth = 3)
@Access(value = SecurityMode.PERMISSIONS, members = {Constants.MANAGE_MTRAINING})
public class Chapter extends CourseUnitMetadata {

    /**
     * List of lessons in the Chapter.
     */
    @Field
    @Persistent(defaultFetchGroup = Constants.TRUE,  mappedBy = "chapter")
    @JsonManagedReference
    private List<Lesson> lessons;

    /**
     * Quiz for the Chapter.
     */
    @Field
    @Persistent(defaultFetchGroup = Constants.TRUE)
    @JsonManagedReference
    private Quiz quiz;

    /**
     * The additional properties which can be used with the Chapter.
     */
    @Field
    @Persistent(defaultFetchGroup = Constants.TRUE)
    private Map<String, String> properties;

    /**
     * Course that owns this Chapter.
     */
    @Field
    @JsonBackReference
    @Persistent(defaultFetchGroup = Constants.TRUE)
    private Course course;

    public Chapter() {
        this(null, CourseUnitState.Inactive, null, null, null);
    }

    public Chapter(String name, CourseUnitState state, String content, String description, Map<String, String> properties) {
        this(name, state, content, description, properties, null, null);
    }

    public Chapter(String name, CourseUnitState state, String content, String description,
                   Map<String, String> properties, List<Lesson> lessons) {
        this(name, state, content, description, properties, lessons, null);
    }

    public Chapter(String name, CourseUnitState state, String content, String description,
                   Map<String, String> properties, List<Lesson> lessons, Quiz quiz) {
        super(name, state, content, description);
        this.lessons = lessons;
        this.quiz = quiz;
        this.properties = properties;
    }

    public List<Lesson> getLessons() {
        if (lessons == null) {
            return new ArrayList<>();
        }
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public Quiz getQuiz() {
        return this.quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    /**
     * {@inheritDoc}
     */
    @Ignore
    @Override
    public CourseUnitDto toUnitDto() {
        List<CourseUnitDto> units = new ArrayList<>();
        if (lessons != null) {
            for (Lesson lesson : lessons) {
                units.add(lesson.toUnitDto());
            }
        }
        return new ChapterUnitDto(getId(), getName(), getState().toString(), units, quiz == null ? null : quiz.toUnitDto());
    }
}
