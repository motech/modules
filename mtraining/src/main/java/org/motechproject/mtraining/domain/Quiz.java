package org.motechproject.mtraining.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.util.List;

/**
 * Quiz object to store questions and answer for a chapter
 */
@Entity
public class Quiz {

    @Field
    private String quizName;

    @Field
    private List<Question> questions;

    @Field
    private double passPercentage;

    public Quiz(String quizName, List<Question> questions, double passPercentage) {
        this.quizName = quizName;
        this.questions = questions;
        this.passPercentage = passPercentage;
    }

    public String getQuizNameName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
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
