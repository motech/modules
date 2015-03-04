package org.motechproject.csd.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.Order;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * Facility
 */
@Entity
@XmlType(propOrder = { "otherIDs", "codedTypes", "primaryName", "otherNames", "addresses", "contacts", "geocode", "languages", "contactPoints", "facilityOrganizations", "operatingHours", "extensions", "record" })
public class Facility extends AbstractUniqueID {

    @Field
    private Geocode geocode;

    @Field
    private FacilityOrganizations facilityOrganizations;

    @Order(column = "facility_operating_hours_idx")
    @Field(name = "facility_operating_hours")
    private List<OperatingHours> operatingHours;

    @Order(column = "facility_other_ids_idx")
    @Field(name = "facility_other_ids")
    private List<OtherID> otherIDs;

    @Field(required = true)
    private String primaryName;

    @Order(column = "facility_other_names_idx")
    @Field(name = "facility_other_names")
    private List<OtherName> otherNames;

    @Order(column = "facility_addresses_idx")
    @Field(name = "facility_addresses")
    private List<Address> addresses;

    @Order(column = "facility_contacts_idx")
    @Field(name = "facility_contacts")
    private List<OrganizationContact> contacts;

    @Order(column = "facility_languages_idx")
    @Field(name = "facility_languages")
    private List<CodedType> languages;

    @Order(column = "facility_contact_points_idx")
    @Field(name = "facility_contact_points")
    private List<ContactPoint> contactPoints;

    @Order(column = "facility_coded_types_idx")
    @Field(required = true, name = "facility_coded_types")
    private List<CodedType> codedTypes;

    @Order(column = "facility_extensions_idx")
    @Field(name = "facility_extensions")
    private List<Extension> extensions;

    @Field(required = true)
    private Record record;

    public Facility() {
    }

    public Facility(String entityID, List<CodedType> codedTypes, Record record, String primaryName) {
        setEntityID(entityID);
        this.codedTypes = codedTypes;
        this.record = record;
        this.primaryName = primaryName;
    }

    public Facility(String entityID, List<CodedType> codedTypes, List<Extension> extensions, Record record, List<OtherID> otherIDs, //NO CHECKSTYLE ArgumentCount
                    String primaryName, List<OtherName> otherNames, List<Address> addresses, List<OrganizationContact> contacts, List<CodedType> languages,
                    List<ContactPoint> contactPoints, Geocode geocode, FacilityOrganizations facilityOrganizations, List<OperatingHours> operatingHours) {
        setEntityID(entityID);
        this.codedTypes = codedTypes;
        this.extensions = extensions;
        this.record = record;
        this.otherIDs = otherIDs;
        this.primaryName = primaryName;
        this.otherNames = otherNames;
        this.addresses = addresses;
        this.contacts = contacts;
        this.languages = languages;
        this.contactPoints = contactPoints;
        this.geocode = geocode;
        this.facilityOrganizations = facilityOrganizations;
        this.operatingHours = operatingHours;
    }

    public Geocode getGeocode() {
        return geocode;
    }

    public void setGeocode(Geocode geocode) {
        this.geocode = geocode;
    }

    public FacilityOrganizations getFacilityOrganizations() {
        return facilityOrganizations;
    }

    @XmlElement(name = "organizations")
    public void setFacilityOrganizations(FacilityOrganizations facilityOrganizations) {
        this.facilityOrganizations = facilityOrganizations;
    }

    public List<OperatingHours> getOperatingHours() {
        return operatingHours;
    }

    public void setOperatingHours(List<OperatingHours> operatingHours) {
        this.operatingHours = operatingHours;
    }

    public List<OtherID> getOtherIDs() {
        return otherIDs;
    }

    @XmlElement(name = "otherID")
    public void setOtherIDs(List<OtherID> otherIDs) {
        this.otherIDs = otherIDs;
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

    public List<CodedType> getLanguages() {
        return languages;
    }

    @XmlElement(name = "language")
    public void setLanguages(List<CodedType> languages) {
        this.languages = languages;
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

    public List<Extension> getExtensions() {
        return extensions;
    }

    @XmlElement(name = "extension")
    public void setExtensions(List<Extension> extensions) {
        this.extensions = extensions;
    }

    public Record getRecord() {
        return record;
    }

    @XmlElement(required = true)
    public void setRecord(Record record) {
        this.record = record;
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

        Facility facility = (Facility) o;

        if (facilityOrganizations != null ? !facilityOrganizations.equals(facility.facilityOrganizations) : facility.facilityOrganizations != null) {
            return false;
        }
        if (geocode != null ? !geocode.equals(facility.geocode) : facility.geocode != null) {
            return false;
        }
        if (operatingHours != null ? !operatingHours.equals(facility.operatingHours) : facility.operatingHours != null) {
            return false;
        }
        if (addresses != null ? !addresses.equals(facility.addresses) : facility.addresses != null) {
            return false;
        }
        if (contactPoints != null ? !contactPoints.equals(facility.contactPoints) : facility.contactPoints != null) {
            return false;
        }
        if (contacts != null ? !contacts.equals(facility.contacts) : facility.contacts != null) {
            return false;
        }
        if (languages != null ? !languages.equals(facility.languages) : facility.languages != null) {
            return false;
        }
        if (otherIDs != null ? !otherIDs.equals(facility.otherIDs) : facility.otherIDs != null) {
            return false;
        }
        if (otherNames != null ? !otherNames.equals(facility.otherNames) : facility.otherNames != null) {
            return false;
        }
        if (!primaryName.equals(facility.primaryName)) {
            return false;
        }
        if (!codedTypes.equals(facility.codedTypes)) {
            return false;
        }
        if (extensions != null ? !extensions.equals(facility.extensions) : facility.extensions != null) {
            return false;
        }
        if (!record.equals(facility.record)) {
            return false;
        }

        return true;
    }

    @Override //NO CHECKSTYLE CyclomaticComplexity
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (geocode != null ? geocode.hashCode() : 0);
        result = 31 * result + (facilityOrganizations != null ? facilityOrganizations.hashCode() : 0);
        result = 31 * result + (operatingHours != null ? operatingHours.hashCode() : 0);
        result = 31 * result + (otherIDs != null ? otherIDs.hashCode() : 0);
        result = 31 * result + primaryName.hashCode();
        result = 31 * result + (otherNames != null ? otherNames.hashCode() : 0);
        result = 31 * result + (addresses != null ? addresses.hashCode() : 0);
        result = 31 * result + (contacts != null ? contacts.hashCode() : 0);
        result = 31 * result + (languages != null ? languages.hashCode() : 0);
        result = 31 * result + (contactPoints != null ? contactPoints.hashCode() : 0);
        result = 31 * result + codedTypes.hashCode();
        result = 31 * result + (extensions != null ? extensions.hashCode() : 0);
        result = 31 * result + record.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Facility{" +
                "geocode=" + geocode +
                ", facilityOrganizations=" + facilityOrganizations +
                ", operatingHours=" + operatingHours +
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
