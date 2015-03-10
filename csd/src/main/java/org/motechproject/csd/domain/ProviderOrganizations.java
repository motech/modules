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
public class ProviderOrganizations {

    @Order(column = "provider_organizations_organization_idx")
    @Field(required = true, name = "provider_organizations_organization")
    private List<ProviderOrganization> providerOrganizations;

    public ProviderOrganizations() {
    }

    public ProviderOrganizations(List<ProviderOrganization> providerOrganizations) {
        this.providerOrganizations = providerOrganizations;
    }

    public List<ProviderOrganization> getProviderOrganizations() {
        return providerOrganizations;
    }

    @XmlElement(name = "organization", required = true)
    public void setProviderOrganizations(List<ProviderOrganization> providerOrganizations) {
        this.providerOrganizations = providerOrganizations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProviderOrganizations that = (ProviderOrganizations) o;

        if (!providerOrganizations.equals(that.providerOrganizations)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return providerOrganizations.hashCode();
    }

    @Override
    public String toString() {
        return "ProviderOrganizations{" +
                "providerOrganizations=" + providerOrganizations +
                '}';
    }
}
