package org.motechproject.csd.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.Order;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@Entity
@XmlType
@XmlAccessorType(XmlAccessType.NONE)
public class FacilityOrganizations {

    @Order(column = "facility_organizations_organization_idx")
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

    @XmlElement(name = "organization", required = true)
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
