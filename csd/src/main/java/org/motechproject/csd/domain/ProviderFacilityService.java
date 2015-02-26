package org.motechproject.csd.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.Order;
import java.util.List;

@Entity
public class ProviderFacilityService extends AbstractUniqueID {

    @Order(column = "provider_facility_service_names_idx")
    @Field(name = "provider_facility_service_names")
    private List<Name> names;

    @Field
    private String providerOrganization;

    @Order(column = "provider_facility_service_languages_idx")
    @Field(name = "provider_facility_service_languages")
    private List<CodedType> languages;

    @Order(column = "provider_facility_service_operating_hours_idx")
    @Field(name = "provider_facility_service_operating_hours")
    private List<OperatingHours> operatingHours;

    @Field
    private String freeBusyURI;

    @Order(column = "provider_facility_service_extensions_idx")
    @Field(name = "provider_facility_service_extensions")
    private List<Extension> extensions;

    public ProviderFacilityService() {
    }

    public ProviderFacilityService(String entityID) {
        setEntityID(entityID);
    }

    public ProviderFacilityService(String entityID, List<Name> names, String providerOrganization, List<CodedType> languages,
                                   List<OperatingHours> operatingHours, String freeBusyURI, List<Extension> extensions) {
        setEntityID(entityID);
        this.names = names;
        this.providerOrganization = providerOrganization;
        this.languages = languages;
        this.operatingHours = operatingHours;
        this.freeBusyURI = freeBusyURI;
        this.extensions = extensions;
    }

    public List<CodedType> getLanguages() {
        return languages;
    }

    public void setLanguages(List<CodedType> languages) {
        this.languages = languages;
    }

    public List<Name> getNames() {
        return names;
    }

    public void setNames(List<Name> names) {
        this.names = names;
    }

    public List<OperatingHours> getOperatingHours() {
        return operatingHours;
    }

    public void setOperatingHours(List<OperatingHours> operatingHours) {
        this.operatingHours = operatingHours;
    }

    public String getProviderOrganization() {
        return providerOrganization;
    }

    public void setProviderOrganization(String providerOrganization) {
        this.providerOrganization = providerOrganization;
    }

    public List<Extension> getExtensions() {
        return extensions;
    }

    public void setExtensions(List<Extension> extensions) {
        this.extensions = extensions;
    }

    public String getFreeBusyURI() {
        return freeBusyURI;
    }

    public void setFreeBusyURI(String freeBusyURI) {
        this.freeBusyURI = freeBusyURI;
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

        ProviderFacilityService that = (ProviderFacilityService) o;

        if (freeBusyURI != null ? !freeBusyURI.equals(that.freeBusyURI) : that.freeBusyURI != null) {
            return false;
        }
        if (extensions != null ? !extensions.equals(that.extensions) : that.extensions != null) {
            return false;
        }
        if (languages != null ? !languages.equals(that.languages) : that.languages != null) {
            return false;
        }
        if (names != null ? !names.equals(that.names) : that.names != null) {
            return false;
        }
        if (providerOrganization != null ? !providerOrganization.equals(that.providerOrganization) : that.providerOrganization != null) {
            return false;
        }
        if (operatingHours != null ? !operatingHours.equals(that.operatingHours) : that.operatingHours != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (names != null ? names.hashCode() : 0);
        result = 31 * result + (providerOrganization != null ? providerOrganization.hashCode() : 0);
        result = 31 * result + (languages != null ? languages.hashCode() : 0);
        result = 31 * result + (operatingHours != null ? operatingHours.hashCode() : 0);
        result = 31 * result + (freeBusyURI != null ? freeBusyURI.hashCode() : 0);
        result = 31 * result + (extensions != null ? extensions.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProviderFacilityService{" +
                "names=" + names +
                ", providerOrganization=" + providerOrganization +
                ", languages=" + languages +
                ", operatingHours=" + operatingHours +
                ", freeBusyURI='" + freeBusyURI + '\'' +
                ", extensions=" + extensions +
                '}';
    }
}
