package org.motechproject.csd.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.util.List;

@Entity
public class Provider extends UniqueID {

    @Field(name = "provider_other_ids")
    private List<OtherID> otherIDs;

    @Field(required = true)
    private Person demographic;

    @Field(name = "provider_languages")
    private List<CodedType> languages;

    @Field
    private ProviderOrganizations providerOrganizations;

    @Field
    private ProviderFacilities providerFacilities;

    @Field(name = "provider_credentials")
    private List<Credential> credentials;

    @Field(name = "provider_specialities")
    private List<CodedType> specialties;

    @Field(required = true, name = "provider_coded_types")
    private List<CodedType> codedTypes;

    @Field(name = "provider_extensions")
    private List<Extension> extensions;

    @Field(required = true)
    private Record record;

    public Provider() {
    }

    public Provider(String entityID, List<CodedType> codedTypes, Record record, Person demographic) {
        setEntityID(entityID);
        this.codedTypes = codedTypes;
        this.record = record;
        this.demographic = demographic;
    }

    public Provider(String entityID, List<CodedType> codedTypes, List<Extension> extensions, Record record, List<OtherID> otherIDs, //NO CHECKSTYLE ArgumentCount
                    Person demographic, List<CodedType> languages, ProviderOrganizations providerOrganizations,
                    ProviderFacilities providerFacilities, List<Credential> credentials, List<CodedType> specialties) {
        setEntityID(entityID);
        this.codedTypes = codedTypes;
        this.extensions = extensions;
        this.record = record;
        this.otherIDs = otherIDs;
        this.demographic = demographic;
        this.languages = languages;
        this.providerOrganizations = providerOrganizations;
        this.providerFacilities = providerFacilities;
        this.credentials = credentials;
        this.specialties = specialties;
    }

    public List<OtherID> getOtherIDs() {
        return otherIDs;
    }

    public void setOtherIDs(List<OtherID> otherIDs) {
        this.otherIDs = otherIDs;
    }

    public Person getDemographic() {
        return demographic;
    }

    public void setDemographic(Person demographic) {
        this.demographic = demographic;
    }

    public List<CodedType> getLanguages() {
        return languages;
    }

    public void setLanguages(List<CodedType> languages) {
        this.languages = languages;
    }

    public ProviderOrganizations getProviderOrganizations() {
        return providerOrganizations;
    }

    public void setProviderOrganizations(ProviderOrganizations providerOrganizations) {
        this.providerOrganizations = providerOrganizations;
    }

    public ProviderFacilities getProviderFacilities() {
        return providerFacilities;
    }

    public void setProviderFacilities(ProviderFacilities providerFacilities) {
        this.providerFacilities = providerFacilities;
    }

    public List<Credential> getCredentials() {
        return credentials;
    }

    public void setCredentials(List<Credential> credentials) {
        this.credentials = credentials;
    }

    public List<CodedType> getSpecialties() {
        return specialties;
    }

    public void setSpecialties(List<CodedType> specialties) {
        this.specialties = specialties;
    }

    public List<CodedType> getCodedTypes() {
        return codedTypes;
    }

    public void setCodedTypes(List<CodedType> codedTypes) {
        this.codedTypes = codedTypes;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
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

        Provider provider = (Provider) o;

        if (credentials != null ? !credentials.equals(provider.credentials) : provider.credentials != null) {
            return false;
        }
        if (!demographic.equals(provider.demographic)) {
            return false;
        }
        if (languages != null ? !languages.equals(provider.languages) : provider.languages != null) {
            return false;
        }
        if (otherIDs != null ? !otherIDs.equals(provider.otherIDs) : provider.otherIDs != null) {
            return false;
        }
        if (providerFacilities != null ? !providerFacilities.equals(provider.providerFacilities) : provider.providerFacilities != null) {
            return false;
        }
        if (providerOrganizations != null ? !providerOrganizations.equals(provider.providerOrganizations) : provider.providerOrganizations != null) {
            return false;
        }
        if (specialties != null ? !specialties.equals(provider.specialties) : provider.specialties != null) {
            return false;
        }
        if (!codedTypes.equals(provider.codedTypes)) {
            return false;
        }
        if (extensions != null ? !extensions.equals(provider.extensions) : provider.extensions != null) {
            return false;
        }
        if (!record.equals(provider.record)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (otherIDs != null ? otherIDs.hashCode() : 0);
        result = 31 * result + demographic.hashCode();
        result = 31 * result + (languages != null ? languages.hashCode() : 0);
        result = 31 * result + (providerOrganizations != null ? providerOrganizations.hashCode() : 0);
        result = 31 * result + (providerFacilities != null ? providerFacilities.hashCode() : 0);
        result = 31 * result + (credentials != null ? credentials.hashCode() : 0);
        result = 31 * result + (specialties != null ? specialties.hashCode() : 0);
        result = 31 * result + codedTypes.hashCode();
        result = 31 * result + (extensions != null ? extensions.hashCode() : 0);
        result = 31 * result + record.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Provider{" +
                "otherIDs=" + otherIDs +
                ", demographic=" + demographic +
                ", languages=" + languages +
                ", providerOrganizations=" + providerOrganizations +
                ", providerFacilities=" + providerFacilities +
                ", credentials=" + credentials +
                ", specialties=" + specialties +
                ", codedTypes=" + codedTypes +
                ", extensions=" + extensions +
                ", record=" + record +
                '}';
    }
}
