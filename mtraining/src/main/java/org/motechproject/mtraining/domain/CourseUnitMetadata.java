package org.motechproject.mtraining.domain;

import org.motechproject.mds.annotations.Field;

/**
 * Created by kosh on 6/2/14.
 */
public class CourseUnitMetadata {

    @Field
    private String name;

    @Field
    private boolean isActive;

    @Field
    private int sequenceNumber;

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
}
