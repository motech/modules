package org.motechproject.mtraining.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.util.List;

/**
 * Created by kosh on 5/29/14.
 */
@Entity
public class Quiz {

    @Field
    private String name;

    @Field
    private List<Question> questions;

    @Field
    private double passPercentage;

    public Quiz() {
        this(null, null, 0.0);
    }

    public Quiz(String name, List<Question> questions, double passPercentage) {
        this.name = name;
        this.questions = questions;
        this.passPercentage = passPercentage;
    }

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
