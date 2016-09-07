package org.motechproject.mtraining.domain;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.motechproject.mds.annotations.Access;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.util.SecurityMode;
import org.motechproject.mtraining.dto.CourseUnitDto;
import org.motechproject.mtraining.util.Constants;

import javax.jdo.annotations.Persistent;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Quiz object to store questions and answer for a chapter
 */
@Entity(maxFetchDepth = 4)
@Access(value = SecurityMode.PERMISSIONS, members = {Constants.MANAGE_MTRAINING})
public class Quiz extends CourseUnitMetadata {

    /**
     * List of questions for the Quiz.
     */
    @Field
    @Persistent(defaultFetchGroup = Constants.TRUE)
    private List<Question> questions;

    /**
     * Pass percentage for the Quiz.
     */
    @Field
    @Min(0)
    @Max(100)
    private double passPercentage;

    /**
     * The additional properties which can be used with the Lesson.
     */
    @Field
    @Persistent(defaultFetchGroup = Constants.TRUE)
    private Map<String, String> properties;

    /**
     * Chapter that owns this Quiz.
     */
    @Field
    @JsonBackReference
    @Persistent(defaultFetchGroup = Constants.TRUE, mappedBy = "quiz")
    private Chapter chapter;

    public Quiz() {
        this(null, CourseUnitState.Inactive, null, null, null);
    }

    public Quiz(String name, CourseUnitState state, String content, String description, Map<String, String> properties) {
        super(name, state, content, description);
        this.properties = properties;
    }

    public Quiz(String name, CourseUnitState state, String content, String description, Map<String, String> properties,
                List<Question> questions, double passPercentage) {
        super(name, state, content, description);
        this.questions = questions;
        this.passPercentage = passPercentage;
        this.properties = properties;
    }

    public List<Question> getQuestions() {

        return questions;
    }

    public void setQuestions(List<Question> questions) {

        this.questions = questions;
    }

    public double getPassPercentage() {

        return passPercentage;
    }

    public void setPassPercentage(double passPercentage) {

        this.passPercentage = passPercentage;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public Chapter getChapter() {
        return chapter;
    }

    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CourseUnitDto toUnitDto() {
        return new CourseUnitDto(getId(), getName(), getState().toString(), null, Constants.QUIZ);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final Quiz other = (Quiz) obj;

        return Objects.equals(this.getId(), other.getId())
                && Objects.equals(this.getName(), other.getName())
                && Objects.equals(this.getPassPercentage(), other.getPassPercentage())
                && Objects.equals(this.getContent(), other.getContent())
                && Objects.equals(this.getDescription(), other.getDescription())
                && Objects.equals(this.getProperties(), other.getProperties())
                && Objects.equals(this.getState(), other.getState());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getPassPercentage(), getContent(), getDescription(), getProperties(), getState());
    }
}
