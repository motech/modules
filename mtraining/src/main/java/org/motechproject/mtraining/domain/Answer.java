package org.motechproject.mtraining.domain;

import org.motechproject.mds.annotations.Access;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;
import org.motechproject.mds.util.SecurityMode;
import org.motechproject.mtraining.util.Constants;

import java.util.Objects;

@Entity
@Access(value = SecurityMode.PERMISSIONS, members = { Constants.MANAGE_MTRAINING })
public class Answer extends MdsEntity {

    /**
     * Answer resource identifier in the external system
     */
    @Field
    private String answer;

    public Answer() {
        this (null);
    }

    public Answer(String answer) {
        this.answer = answer;
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

        final Answer other = (Answer) obj;

        return Objects.equals(this.getId(), other.getId())
                && Objects.equals(this.getAnswer(), other.getAnswer());
    }


    @Override
    public String toString() {
        return answer;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAnswer());
    }
}
