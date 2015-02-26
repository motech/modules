package org.motechproject.csd.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.util.List;

@Entity
public class ProviderFacility extends UniqueID {

    @Field(name = "provider_facility_services")
    private List<FacilityOrganizationService> services;

    @Field(name = "provider_facility_operating_hours")
    private List<OperatingHours> operatingHours;

    @Field(name = "provider_facility_extensions")
    private List<Extension> extensions;

    public ProviderFacility() {
    }

    public ProviderFacility(String entityID) {
        setEntityID(entityID);
    }

    public ProviderFacility(String entityID, List<FacilityOrganizationService> services, List<OperatingHours> operatingHours, List<Extension> extensions) {
        setEntityID(entityID);
        this.services = services;
        this.operatingHours = operatingHours;
        this.extensions = extensions;
    }

    public List<FacilityOrganizationService> getServices() {
        return services;
    }

    public void setServices(List<FacilityOrganizationService> services) {
        this.services = services;
    }

    public List<OperatingHours> getOperatingHours() {
        return operatingHours;
    }

    public void setOperatingHours(List<OperatingHours> operatingHours) {
        this.operatingHours = operatingHours;
    }

    public List<Extension> getExtensions() {
        return extensions;
    }

    public void setExtensions(List<Extension> extensions) {
        this.extensions = extensions;
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

        ProviderFacility that = (ProviderFacility) o;

        if (extensions != null ? !extensions.equals(that.extensions) : that.extensions != null) {
            return false;
        }
        if (operatingHours != null ? !operatingHours.equals(that.operatingHours) : that.operatingHours != null) {
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
        result = 31 * result + (operatingHours != null ? operatingHours.hashCode() : 0);
        result = 31 * result + (extensions != null ? extensions.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProviderFacility{" +
                "services=" + services +
                ", operatingHours=" + operatingHours +
                ", extensions=" + extensions +
                '}';
    }
}
