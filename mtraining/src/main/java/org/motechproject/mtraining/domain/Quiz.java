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
    private String name;

    @Field
    private List<Question> questions;

    @Field
    private Double passPercentage;

    public Quiz(String name, List<Question> questions, Double passPercentage) {
        this.name = name;
        this.questions = questions;
        this.passPercentage = passPercentage;
    }

    public String getName() {

        return this.name;
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

    public void setPassPercentage(Double passPercentage) {

        this.passPercentage = passPercentage;
    }
}
