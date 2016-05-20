package org.motechproject.dhis2.rest.domain.v2_21;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.motechproject.dhis2.rest.domain.AttributeDto;
import org.motechproject.dhis2.rest.domain.base.BaseEnrollmentDto;

import java.util.List;
import java.util.Objects;

/**
 * A class to model enrollments posted to the DHIS2 server in version 2.21 or higher.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class EnrollmentDto extends BaseEnrollmentDto {

    private String enrollmentDate;
    private String incidentDate;
    private String orgUnit;

    public EnrollmentDto() {
    }

    public EnrollmentDto(String trackedEntityInstance, String program, String enrollmentDate, String incidentDate, String orgUnit, List<AttributeDto> attributes, boolean followup) {
        super(trackedEntityInstance, program, attributes, followup);
        this.enrollmentDate = enrollmentDate;
        this.incidentDate = incidentDate;
        this.orgUnit = orgUnit;
    }

    public String getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(String enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public String getIncidentDate() {
        return incidentDate;
    }

    public void setIncidentDate(String incidentDate) {
        this.incidentDate = incidentDate;
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
        return Objects.equals(enrollmentDate, that.enrollmentDate) &&
                Objects.equals(incidentDate, that.incidentDate) &&
                Objects.equals(orgUnit, that.orgUnit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), enrollmentDate, incidentDate, orgUnit);
    }
}
