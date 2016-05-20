package org.motechproject.dhis2.rest.domain.v2_19;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.motechproject.dhis2.rest.domain.AttributeDto;
import org.motechproject.dhis2.rest.domain.base.BaseEnrollmentDto;

import java.util.List;
import java.util.Objects;

/**
 * A class to model enrollments posted to the DHIS2 server in version 2.19 or higher.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class EnrollmentDto extends BaseEnrollmentDto {

    private String dateOfEnrollment;
    private String dateOfIncident;
    private String orgUnit;

    public EnrollmentDto() {
    }

    public EnrollmentDto(String trackedEntityInstance, String program, String dateOfEnrollment, String dateOfIncident, String orgUnit, List<AttributeDto> attributes, boolean followup) {
        super(trackedEntityInstance, program, attributes, followup);
        this.dateOfEnrollment = dateOfEnrollment;
        this.dateOfIncident = dateOfIncident;
        this.orgUnit = orgUnit;
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

    public String getOrgUnit() {
        return orgUnit;
    }

    public void setOrgUnit(String orgUnit) {
        this.orgUnit = orgUnit;
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
                Objects.equals(dateOfIncident, that.dateOfIncident) &&
                Objects.equals(orgUnit, that.orgUnit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), dateOfEnrollment, dateOfIncident, orgUnit);
    }
}
