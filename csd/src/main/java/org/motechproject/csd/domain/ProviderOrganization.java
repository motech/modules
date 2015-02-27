package org.motechproject.csd.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.util.List;

@Entity
public class ProviderOrganization extends UniqueID {

    @Field(name = "provider_organization_extensions")
    private List<Extension> extensions;

    @Field(name = "provider_organization_addresses")
    private List<Address> addresses;

    @Field(name = "provider_organization_contact_points")
    private List<ContactPoint> contactPoints;

    public ProviderOrganization() {
    }

    public ProviderOrganization(String entityID) {
        setEntityID(entityID);
    }

    public ProviderOrganization(String entityID, List<Extension> extensions, List<Address> addresses, List<ContactPoint> contactPoints) {
        setEntityID(entityID);
        this.extensions = extensions;
        this.addresses = addresses;
        this.contactPoints = contactPoints;
    }

    public List<Extension> getExtensions() {
        return extensions;
    }

    public void setExtensions(List<Extension> extensions) {
        this.extensions = extensions;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public List<ContactPoint> getContactPoints() {
        return contactPoints;
    }

    public void setContactPoints(List<ContactPoint> contactPoints) {
        this.contactPoints = contactPoints;
    }

    @Override //NO CHECKSTYLE CyclomaticComplexity
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        ProviderOrganization that = (ProviderOrganization) o;

        if (addresses != null ? !addresses.equals(that.addresses) : that.addresses != null) {
            return false;
        }
        if (contactPoints != null ? !contactPoints.equals(that.contactPoints) : that.contactPoints != null) {
            return false;
        }
        if (extensions != null ? !extensions.equals(that.extensions) : that.extensions != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (extensions != null ? extensions.hashCode() : 0);
        result = 31 * result + (addresses != null ? addresses.hashCode() : 0);
        result = 31 * result + (contactPoints != null ? contactPoints.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProviderOrganization{" +
                "extensions=" + extensions +
                ", addresses=" + addresses +
                ", contactPoints=" + contactPoints +
                '}';
    }
}
