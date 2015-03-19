package org.motechproject.dhis2.rest.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.List;
import java.util.Objects;

/**
 * A class to model enrollments posted to the DHIS2 API.
 */
@JsonInclude(Include.NON_NULL)
public class EnrollmentDto {
    private String trackedEntityInstance;
    private String program;
    private String dateOfEnrollment;
    private String dateOfIncident;
    private List<AttributeDto> attributes;
    private boolean followup;

    public String getTrackedEntityInstance() {
        return trackedEntityInstance;
    }

    public void setTrackedEntityInstance(String trackedEntityInstance) {
        this.trackedEntityInstance = trackedEntityInstance;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getDateOfEnrollment() {
        return dateOfEnrollment;
    }

    public void setDateOfEnrollment(String dateOfEnrollment) {
        this.dateOfEnrollment = dateOfEnrollment;
    }

    public String getDateOfIncident() {
        return dateOfIncident;
    }

    public void setDateOfIncident(String dateOfIncident) {
        this.dateOfIncident = dateOfIncident;
    }

    public List<AttributeDto> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributeDto> attributes) {
        this.attributes = attributes;
    }

    public boolean isFollowup() {
        return followup;
    }

    public void setFollowup(boolean followup) {
        this.followup = followup;
    }

    @Override
    public int hashCode() {
        return Objects.hash(trackedEntityInstance, program, dateOfEnrollment, dateOfIncident, attributes, followup);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final EnrollmentDto other = (EnrollmentDto) obj;
        return Objects.equals(this.trackedEntityInstance, other.trackedEntityInstance)
                && Objects.equals(this.program, other.program)
                && Objects.equals(this.dateOfEnrollment, other.dateOfEnrollment)
                && Objects.equals(this.dateOfIncident, other.dateOfIncident)
                && Objects.equals(this.attributes, other.attributes)
                && Objects.equals(this.followup, other.followup);
    }
}
