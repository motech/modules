package org.motechproject.csd.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.util.List;

@Entity
public class FacilityOrganizationService extends UniqueID {

    @Field(name = "facility_organization_service_names")
    private List<Name> names;

    @Field(name = "facility_organization_service_languages")
    private List<CodedType> languages;

    @Field(name = "facility_organization_service")
    private List<OperatingHours> operatingHours;

    @Field
    private String freeBusyURI;

    @Field(name = "facility_organization_services")
    private List<Extension> extensions;

    public FacilityOrganizationService() {
    }

    public FacilityOrganizationService(String entityID) {
        setEntityID(entityID);
    }

    public FacilityOrganizationService(String entityID, List<Name> names, List<CodedType> languages, List<OperatingHours> operatingHours, String freeBusyURI, List<Extension> extensions) {
        setEntityID(entityID);
        this.names = names;
        this.languages = languages;
        this.operatingHours = operatingHours;
        this.freeBusyURI = freeBusyURI;
        this.extensions = extensions;
    }

    public List<Name> getNames() {
        return names;
    }

    public void setNames(List<Name> names) {
        this.names = names;
    }

    public List<CodedType> getLanguages() {
        return languages;
    }

    public void setLanguages(List<CodedType> languages) {
        this.languages = languages;
    }

    public List<OperatingHours> getOperatingHours() {
        return operatingHours;
    }

    public void setOperatingHours(List<OperatingHours> operatingHours) {
        this.operatingHours = operatingHours;
    }

    public String getFreeBusyURI() {
        return freeBusyURI;
    }

    public void setFreeBusyURI(String freeBusyURI) {
        this.freeBusyURI = freeBusyURI;
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

        FacilityOrganizationService that = (FacilityOrganizationService) o;

        if (extensions != null ? !extensions.equals(that.extensions) : that.extensions != null) {
            return false;
        }
        if (freeBusyURI != null ? !freeBusyURI.equals(that.freeBusyURI) : that.freeBusyURI != null) {
            return false;
        }
        if (languages != null ? !languages.equals(that.languages) : that.languages != null) {
            return false;
        }
        if (names != null ? !names.equals(that.names) : that.names != null) {
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
        result = 31 * result + (languages != null ? languages.hashCode() : 0);
        result = 31 * result + (operatingHours != null ? operatingHours.hashCode() : 0);
        result = 31 * result + (freeBusyURI != null ? freeBusyURI.hashCode() : 0);
        result = 31 * result + (extensions != null ? extensions.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FacilityOrganizationService{" +
                "names=" + names +
                ", languages=" + languages +
                ", operatingHours=" + operatingHours +
                ", freeBusyURI='" + freeBusyURI + '\'' +
                ", extensions=" + extensions +
                '}';
    }
}
