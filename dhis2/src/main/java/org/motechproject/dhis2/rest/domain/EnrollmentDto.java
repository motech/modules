package org.motechproject.dhis2.rest.domain;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.List;
import java.util.Objects;

/**
 * A class to model enrollments posted to the DHIS2 API.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class EnrollmentDto {
    private String trackedEntityInstance;
    private String program;
    private String dateOfEnrollment;
    private String dateOfIncident;
    private String orgUnit;
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

    public String getOrgUnit() {
        return orgUnit;
    }

    public void setOrgUnit(String orgUnit) {
        this.orgUnit = orgUnit;
    }

    public org.motechproject.dhis2.rest.domain.v2_18.EnrollmentDto convertTo218() {
        return new org.motechproject.dhis2.rest.domain.v2_18.EnrollmentDto(
                trackedEntityInstance, attributes, program, dateOfEnrollment, dateOfIncident, followup
        );
    }

    public org.motechproject.dhis2.rest.domain.v2_19.EnrollmentDto convertTo219() {
        return new org.motechproject.dhis2.rest.domain.v2_19.EnrollmentDto(
                trackedEntityInstance, program, dateOfEnrollment, dateOfIncident, orgUnit, attributes, followup
        );
    }

    public org.motechproject.dhis2.rest.domain.v2_21.EnrollmentDto convertTo221() {
        return new org.motechproject.dhis2.rest.domain.v2_21.EnrollmentDto(
                trackedEntityInstance, program, dateOfEnrollment, dateOfIncident, orgUnit, attributes, followup
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(trackedEntityInstance, program, dateOfEnrollment, dateOfIncident, orgUnit, attributes, followup);
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
                && Objects.equals(this.orgUnit, other.orgUnit)
                && Objects.equals(this.attributes, other.attributes)
                && Objects.equals(this.followup, other.followup);
    }
}
