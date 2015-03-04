package org.motechproject.csd.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.Ignore;

import javax.jdo.annotations.Order;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@Entity
@XmlType(propOrder = { "otherIDs", "codedTypes", "primaryName", "otherNames", "addresses", "contacts", "credentials", "languages", "specializations", "contactPoints", "parent", "extensions", "record" })
public class Organization extends AbstractUniqueID {

    @Order(column = "organization_other_ids_idx")
    @Field(name = "organization_other_ids")
    private List<OtherID> otherIDs;

    @Field(required = true)
    private String primaryName;

    @Order(column = "organization_other_names_idx")
    @Field(name = "organization_other_names")
    private List<OtherName> otherNames;

    @Order(column = "organization_addresses_idx")
    @Field(name = "organization_addresses")
    private List<Address> addresses;

    @Order(column = "organization_contacts_idx")
    @Field(name = "organization_contacts")
    private List<OrganizationContact> contacts;

    @Order(column = "organization_credentials_idx")
    @Field(name = "organization_credentials")
    private List<Credential> credentials;

    @Order(column = "organization_languages_idx")
    @Field(name = "organization_languages")
    private List<CodedType> languages;

    @Order(column = "organization_specialization_idx")
    @Field(name = "organization_specialization")
    private List<CodedType> specializations;

    @Order(column = "organization_contact_points_idx")
    @Field(name = "organization_contact_points")
    private List<ContactPoint> contactPoints;

    @Order(column = "organization_coded_types_idx")
    @Field(required = true, name = "organization_coded_types")
    private List<CodedType> codedTypes;

    @Field
    private String parentOrganization;

    @Ignore
    private UniqueID parent;

    @Order(column = "organization_extensions_idx")
    @Field(name = "organization_extensions")
    private List<Extension> extensions;

    @Field(required = true)
    private Record record;

    public Organization() {
    }

    public Organization(String entityID, List<CodedType> codedTypes, String primaryName, Record record) {
        setEntityID(entityID);
        this.codedTypes = codedTypes;
        this.primaryName = primaryName;
        this.record = record;
    }

    public Organization(String entityID, List<Credential> credentials, String parentOrganization, List<CodedType> specializations, List<OtherID> otherIDs, //NO CHECKSTYLE ArgumentCount
                        String primaryName, List<OtherName> otherNames, List<Address> addresses, List<OrganizationContact> contacts,
                        List<CodedType> languages, List<ContactPoint> contactPoints, List<CodedType> codedTypes, List<Extension> extensions, Record record) {
        setEntityID(entityID);
        this.credentials = credentials;
        this.parentOrganization = parentOrganization;
        parent = new UniqueID(parentOrganization);
        this.specializations = specializations;
        this.otherIDs = otherIDs;
        this.primaryName = primaryName;
        this.otherNames = otherNames;
        this.addresses = addresses;
        this.contacts = contacts;
        this.languages = languages;
        this.contactPoints = contactPoints;
        this.codedTypes = codedTypes;
        this.extensions = extensions;
        this.record = record;
    }

    public String getPrimaryName() {
        return primaryName;
    }

    @XmlElement(required = true)
    public void setPrimaryName(String primaryName) {
        this.primaryName = primaryName;
    }

    public List<OtherName> getOtherNames() {
        return otherNames;
    }

    @XmlElement(name = "otherName")
    public void setOtherNames(List<OtherName> otherNames) {
        this.otherNames = otherNames;
    }

    public List<OtherID> getOtherIDs() {
        return otherIDs;
    }

    @XmlElement(name = "otherID")
    public void setOtherIDs(List<OtherID> otherIDs) {
        this.otherIDs = otherIDs;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    @XmlElement(name = "address")
    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public List<OrganizationContact> getContacts() {
        return contacts;
    }

    @XmlElement(name = "contact")
    public void setContacts(List<OrganizationContact> contacts) {
        this.contacts = contacts;
    }

    public List<Credential> getCredentials() {
        return credentials;
    }

    @XmlElement(name = "credential")
    public void setCredentials(List<Credential> credentials) {
        this.credentials = credentials;
    }

    public List<CodedType> getLanguages() {
        return languages;
    }

    @XmlElement(name = "language")
    public void setLanguages(List<CodedType> languages) {
        this.languages = languages;
    }

    public List<CodedType> getSpecializations() {
        return specializations;
    }

    @XmlElement(name = "specialization")
    public void setSpecializations(List<CodedType> specializations) {
        this.specializations = specializations;
    }

    public List<ContactPoint> getContactPoints() {
        return contactPoints;
    }

    @XmlElement(name = "contactPoint")
    public void setContactPoints(List<ContactPoint> contactPoints) {
        this.contactPoints = contactPoints;
    }

    public List<CodedType> getCodedTypes() {
        return codedTypes;
    }

    @XmlElement(name = "codedType", required = true)
    public void setCodedTypes(List<CodedType> codedTypes) {
        this.codedTypes = codedTypes;
    }

    public Record getRecord() {
        return record;
    }

    @XmlElement(required = true)
    public void setRecord(Record record) {
        this.record = record;
    }

    @XmlTransient
    public String getParentOrganization() {
        return parentOrganization;
    }

    public void setParentOrganization(String parentOrganization) {
        this.parentOrganization = parentOrganization;
        parent = new UniqueID(parentOrganization);
    }

    public UniqueID getParent() {
        return parent;
    }

    public void setParent(UniqueID parent) {
        this.parent = parent;
        parentOrganization = parent.getEntityID();
    }

    public List<Extension> getExtensions() {
        return extensions;
    }

    @XmlElement(name = "extension")
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

        Organization that = (Organization) o;

        if (addresses != null ? !addresses.equals(that.addresses) : that.addresses != null) {
            return false;
        }
        if (!codedTypes.equals(that.codedTypes)) {
            return false;
        }
        if (contactPoints != null ? !contactPoints.equals(that.contactPoints) : that.contactPoints != null) {
            return false;
        }
        if (contacts != null ? !contacts.equals(that.contacts) : that.contacts != null) {
            return false;
        }
        if (credentials != null ? !credentials.equals(that.credentials) : that.credentials != null) {
            return false;
        }
        if (extensions != null ? !extensions.equals(that.extensions) : that.extensions != null) {
            return false;
        }
        if (languages != null ? !languages.equals(that.languages) : that.languages != null) {
            return false;
        }
        if (otherIDs != null ? !otherIDs.equals(that.otherIDs) : that.otherIDs != null) {
            return false;
        }
        if (otherNames != null ? !otherNames.equals(that.otherNames) : that.otherNames != null) {
            return false;
        }
        if (parentOrganization != null ? !parentOrganization.equals(that.parentOrganization) : that.parentOrganization != null) {
            return false;
        }
        if (!primaryName.equals(that.primaryName)) {
            return false;
        }
        if (!record.equals(that.record)) {
            return false;
        }
        if (specializations != null ? !specializations.equals(that.specializations) : that.specializations != null) {
            return false;
        }

        return true;
    }

    @Override //NO CHECKSTYLE CyclomaticComplexity
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (credentials != null ? credentials.hashCode() : 0);
        result = 31 * result + (otherIDs != null ? otherIDs.hashCode() : 0);
        result = 31 * result + primaryName.hashCode();
        result = 31 * result + (otherNames != null ? otherNames.hashCode() : 0);
        result = 31 * result + (addresses != null ? addresses.hashCode() : 0);
        result = 31 * result + (specializations != null ? specializations.hashCode() : 0);
        result = 31 * result + (contacts != null ? contacts.hashCode() : 0);
        result = 31 * result + (languages != null ? languages.hashCode() : 0);
        result = 31 * result + (contactPoints != null ? contactPoints.hashCode() : 0);
        result = 31 * result + (parentOrganization != null ? parentOrganization.hashCode() : 0);
        result = 31 * result + codedTypes.hashCode();
        result = 31 * result + record.hashCode();
        result = 31 * result + (extensions != null ? extensions.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Organization{" +
                "credentials=" + credentials +
                ", parentOrganization=" + parentOrganization +
                ", specializations=" + specializations +
                ", otherIDs=" + otherIDs +
                ", primaryName='" + primaryName + '\'' +
                ", otherNames=" + otherNames +
                ", addresses=" + addresses +
                ", contacts=" + contacts +
                ", languages=" + languages +
                ", contactPoints=" + contactPoints +
                ", codedTypes=" + codedTypes +
                ", extensions=" + extensions +
                ", record=" + record +
                '}';
    }
}
