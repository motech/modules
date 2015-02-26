package org.motechproject.csd.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.Order;
import java.util.List;

@Entity
public class FacilityOrganization extends AbstractUniqueID {

    @Order(column = "facility_organization_services_idx")
    @Field(name = "facility_organization_services")
    private List<FacilityOrganizationService> services;

    @Order(column = "facility_organization_extensions_idx")
    @Field(name = "facility_organization_extensions")
    private List<Extension> extensions;

    public FacilityOrganization() {
    }

    public FacilityOrganization(String entityID) {
        setEntityID(entityID);
    }

    public FacilityOrganization(String entityID, List<FacilityOrganizationService> services, List<Extension> extensions) {
        setEntityID(entityID);
        this.services = services;
        this.extensions = extensions;
    }

    public List<FacilityOrganizationService> getServices() {
        return services;
    }

    public void setServices(List<FacilityOrganizationService> services) {
        this.services = services;
    }

    public List<Extension> getExtensions() {
        return extensions;
    }

    public void setExtensions(List<Extension> extensions) {
        this.extensions = extensions;
    }

    @Override
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

        FacilityOrganization that = (FacilityOrganization) o;

        if (extensions != null ? !extensions.equals(that.extensions) : that.extensions != null) {
            return false;
        }
        if (services != null ? !services.equals(that.services) : that.services != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (services != null ? services.hashCode() : 0);
        result = 31 * result + (extensions != null ? extensions.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FacilityOrganization{" +
                "services=" + services +
                ", extensions=" + extensions +
                '}';
    }
}
