package org.motechproject.csd.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.Order;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@Entity
@XmlType
public class OrganizationDirectory {

    @Order(column = "organization_directory_organizations_idx")
    @Field(name = "organization_directory_organizations")
    private List<Organization> organizations;

    public OrganizationDirectory() {
    }

    public OrganizationDirectory(List<Organization> organizations) {
        this.organizations = organizations;
    }

    public List<Organization> getOrganizations() {
        return organizations;
    }

    @XmlElement(name = "organization")
    public void setOrganizations(List<Organization> organizations) {
        this.organizations = organizations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrganizationDirectory that = (OrganizationDirectory) o;

        if (organizations != null ? !organizations.equals(that.organizations) : that.organizations != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return organizations != null ? organizations.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "OrganizationDirectory{" +
                "organizations=" + organizations +
                '}';
    }
}
