package org.motechproject.mtraining.domain;

/**
 * Common metadata shared by all course sub classes
 */
public class CourseUnitMetadata {

    private String name;

    private boolean status;

    private int sequenceNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
}
