package org.motechproject.mtraining.domain;

import java.util.List;

/**
 * Created by kosh on 5/29/14.
 */
public class Quiz {

    private String name;

    private List<Question> questions;

    private double passPercentage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
