package org.motechproject.csd.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.util.List;

@Entity
public class FacilityOrganizations {

    @Field(required = true, name = "facility_organizations_organization")
    private List<FacilityOrganization> facilityOrganizations;

    public FacilityOrganizations() {
    }

    public FacilityOrganizations(List<FacilityOrganization> facilityOrganizations) {
        this.facilityOrganizations = facilityOrganizations;
    }

    public List<FacilityOrganization> getFacilityOrganizations() {
        return facilityOrganizations;
    }

    public void setFacilityOrganizations(List<FacilityOrganization> facilityOrganizations) {
        this.facilityOrganizations = facilityOrganizations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FacilityOrganizations that = (FacilityOrganizations) o;

        if (!facilityOrganizations.equals(that.facilityOrganizations)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return facilityOrganizations.hashCode();
    }

    @Override
    public String toString() {
        return "FacilityOrganizations{" +
                "facilityOrganizations=" + facilityOrganizations +
                '}';
    }
}
