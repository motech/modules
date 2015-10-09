package org.motechproject.mtraining.domain;

import org.motechproject.mds.annotations.Access;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.util.SecurityMode;
import org.motechproject.mtraining.util.Constants;

import javax.jdo.annotations.Persistent;
import java.util.List;
import java.util.Map;

/**
 * Quiz object to store questions and answer for a chapter
 */
@Entity
@Access(value = SecurityMode.PERMISSIONS, members = {Constants.MANAGE_MTRAINING})
public class Quiz extends CourseUnitMetadata {

    /**
     * List of questions for the Quiz.
     */
    @Field
    private List<Question> questions;

    /**
     * Pass percentage for the Quiz.
     */
    @Field
    private double passPercentage;

    /**
     * The additional properties which can be used with the Lesson.
     */
    @Field
    @Persistent(defaultFetchGroup = "true")
    private Map<String, String> properties;

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
}
