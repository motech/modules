package org.motechproject.csd.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@Entity
@XmlRootElement(name = "CSD")
@XmlType(propOrder = { "organizationDirectory", "serviceDirectory", "facilityDirectory", "providerDirectory" })
public class CSD {

    @Field(required = true)
    private OrganizationDirectory organizationDirectory;

    @Field(required = true)
    private ServiceDirectory serviceDirectory;

    @Field(required = true)
    private FacilityDirectory facilityDirectory;

    @Field(required = true)
    private ProviderDirectory providerDirectory;

    public CSD() {
    }

    public CSD(OrganizationDirectory organizationDirectory, ServiceDirectory serviceDirectory, FacilityDirectory facilityDirectory,
               ProviderDirectory providerDirectory) {
        this.organizationDirectory = organizationDirectory;
        this.serviceDirectory = serviceDirectory;
        this.facilityDirectory = facilityDirectory;
        this.providerDirectory = providerDirectory;
    }

    public OrganizationDirectory getOrganizationDirectory() {
        return organizationDirectory;
    }

    @XmlElement(required = true)
    public void setOrganizationDirectory(OrganizationDirectory organizationDirectory) {
        this.organizationDirectory = organizationDirectory;
    }

    public ServiceDirectory getServiceDirectory() {
        return serviceDirectory;
    }

    @XmlElement(required = true)
    public void setServiceDirectory(ServiceDirectory serviceDirectory) {
        this.serviceDirectory = serviceDirectory;
    }

    public FacilityDirectory getFacilityDirectory() {
        return facilityDirectory;
    }

    @XmlElement(required = true)
    public void setFacilityDirectory(FacilityDirectory facilityDirectory) {
        this.facilityDirectory = facilityDirectory;
    }

    public ProviderDirectory getProviderDirectory() {
        return providerDirectory;
    }

    @XmlElement(required = true)
    public void setProviderDirectory(ProviderDirectory providerDirectory) {
        this.providerDirectory = providerDirectory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CSD csd = (CSD) o;

        if (!facilityDirectory.equals(csd.facilityDirectory)) {
            return false;
        }
        if (!organizationDirectory.equals(csd.organizationDirectory)) {
            return false;
        }
        if (!providerDirectory.equals(csd.providerDirectory)) {
            return false;
        }
        if (!serviceDirectory.equals(csd.serviceDirectory)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = organizationDirectory.hashCode();
        result = 31 * result + serviceDirectory.hashCode();
        result = 31 * result + facilityDirectory.hashCode();
        result = 31 * result + providerDirectory.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "CSD{" +
                "organizationDirectory=" + organizationDirectory +
                ", serviceDirectory=" + serviceDirectory +
                ", facilityDirectory=" + facilityDirectory +
                ", providerDirectory=" + providerDirectory +
                '}';
    }
}
