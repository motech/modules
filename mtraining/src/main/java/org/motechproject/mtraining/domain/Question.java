package org.motechproject.mtraining.domain;

import org.motechproject.mds.annotations.Access;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;
import org.motechproject.mds.util.SecurityMode;
import org.motechproject.mtraining.util.Constants;

import java.util.Objects;

/**
 * Question object with resource identifiers for question and answer
 */
@Entity
@Access(value = SecurityMode.PERMISSIONS, members = {Constants.MANAGE_MTRAINING})
public class Question extends MdsEntity {

    /**
     * Question resource identifier in the external system
     */
    @Field
    private String question;

    /**
     * Answer resource identifier in the external system
     */
    @Field
    private String answer;

    public Question() {
        this (null, null);
    }

    public Question(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final Question other = (Question) obj;

        return Objects.equals(this.getId(), other.getId())
                && Objects.equals(this.getQuestion(), other.getQuestion())
                && Objects.equals(this.getAnswer(), other.getAnswer());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getQuestion(), getAnswer());
    }
}
