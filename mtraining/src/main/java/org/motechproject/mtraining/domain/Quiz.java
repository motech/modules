package org.motechproject.mtraining.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.util.List;

/**
 * Quiz object to store questions and answer for a chapter
 */
@Entity
public class Quiz extends CourseUnitMetadata {

    @Field
    private List<Question> questions;

    @Field
    private double passPercentage;

    public Quiz() {
    }

    public Quiz(String name, CourseUnitState state, String content) {
        super(name, state, content);
    }

    public Quiz(String name, CourseUnitState state, String content, List<Question> questions, double passPercentage) {
        super(name, state, content);
        this.questions = questions;
        this.passPercentage = passPercentage;
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
