package org.motechproject.dhis2.rest.domain.v2_18;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.motechproject.dhis2.rest.domain.AttributeDto;
import org.motechproject.dhis2.rest.domain.base.BaseEnrollmentDto;

import java.util.List;
import java.util.Objects;

/**
 * A class to model enrollments posted to the DHIS2 server up to version 2.18.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class EnrollmentDto extends BaseEnrollmentDto {

    private String dateOfEnrollment;
    private String dateOfIncident;

    public EnrollmentDto() {
    }

    public EnrollmentDto(String trackedEntityInstance, List<AttributeDto> attributes, String program, String dateOfEnrollment, String dateOfIncident, boolean followup) {
        super(trackedEntityInstance, program, attributes, followup);
        this.dateOfEnrollment = dateOfEnrollment;
        this.dateOfIncident = dateOfIncident;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EnrollmentDto)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        EnrollmentDto that = (EnrollmentDto) o;
        return Objects.equals(dateOfEnrollment, that.dateOfEnrollment) &&
                Objects.equals(dateOfIncident, that.dateOfIncident);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), dateOfEnrollment, dateOfIncident);
    }
}
