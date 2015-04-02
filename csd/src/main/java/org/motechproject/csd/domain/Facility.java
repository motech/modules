package org.motechproject.csd.domain;

import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.UIDisplayable;

import javax.jdo.annotations.Order;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Java class for facility complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="facility">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:ihe:iti:csd:2013}uniqueID">
 *       &lt;sequence>
 *         &lt;element name="otherID" type="{urn:ihe:iti:csd:2013}otherID" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="codedType" type="{urn:ihe:iti:csd:2013}codedtype" maxOccurs="unbounded"/>
 *         &lt;element name="primaryName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="otherName" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute ref="{http://www.w3.org/XML/1998/namespace}lang"/>
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="address" type="{urn:ihe:iti:csd:2013}address" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="contact" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="provider" type="{urn:ihe:iti:csd:2013}uniqueID"/>
 *                   &lt;element name="person" type="{urn:ihe:iti:csd:2013}person"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="geocode" type="{urn:ihe:iti:csd:2013}geocode" minOccurs="0"/>
 *         &lt;element name="language" type="{urn:ihe:iti:csd:2013}codedtype" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="contactPoint" type="{urn:ihe:iti:csd:2013}contactPoint" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="organizations" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="organization" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;extension base="{urn:ihe:iti:csd:2013}uniqueID">
 *                           &lt;sequence>
 *                             &lt;element name="service" maxOccurs="unbounded" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;extension base="{urn:ihe:iti:csd:2013}uniqueID">
 *                                     &lt;sequence>
 *                                       &lt;element name="name" type="{urn:ihe:iti:csd:2013}name" maxOccurs="unbounded" minOccurs="0"/>
 *                                       &lt;element name="language" type="{urn:ihe:iti:csd:2013}codedtype" maxOccurs="unbounded" minOccurs="0"/>
 *                                       &lt;element name="operatingHours" type="{urn:ihe:iti:csd:2013}operatingHours" maxOccurs="unbounded" minOccurs="0"/>
 *                                       &lt;element name="freeBusyURI" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *                                       &lt;element name="extension" type="{urn:ihe:iti:csd:2013}extension" maxOccurs="unbounded" minOccurs="0"/>
 *                                     &lt;/sequence>
 *                                   &lt;/extension>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="extension" type="{urn:ihe:iti:csd:2013}extension" maxOccurs="unbounded" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/extension>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="operatingHours" type="{urn:ihe:iti:csd:2013}operatingHours" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="extension" type="{urn:ihe:iti:csd:2013}extension" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="record" type="{urn:ihe:iti:csd:2013}record"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@Entity
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(propOrder = { "otherIDs", "codedTypes", "primaryName", "otherNames", "addresses", "contacts", "geocode", "languages", "contactPoints", "facilityOrganizations", "operatingHours", "extensions", "record" })
public class Facility extends AbstractUniqueID {

    @Field
    @Cascade(delete = true)
    private Geocode geocode;

    @Field
    @Cascade(delete = true)
    private FacilityOrganizations facilityOrganizations;

    @UIDisplayable(position = 2)
    @Order(column = "facility_operating_hours_idx")
    @Field(name = "facility_operating_hours")
    @Cascade(delete = true)
    private List<OperatingHours> operatingHours = new ArrayList<>();

    @Order(column = "facility_other_ids_idx")
    @Field(name = "facility_other_ids")
    @Cascade(delete = true)
    private List<OtherID> otherIDs = new ArrayList<>();

    @UIDisplayable(position = 0)
    @Field(required = true)
    private String primaryName;

    @Order(column = "facility_other_names_idx")
    @Field(name = "facility_other_names")
    @Cascade(delete = true)
    private List<OtherName> otherNames = new ArrayList<>();

    @UIDisplayable(position = 1)
    @Order(column = "facility_addresses_idx")
    @Field(name = "facility_addresses")
    @Cascade(delete = true)
    private List<Address> addresses = new ArrayList<>();

    @Order(column = "facility_contacts_idx")
    @Field(name = "facility_contacts")
    @Cascade(delete = true)
    private List<OrganizationContact> contacts = new ArrayList<>();

    @Order(column = "facility_languages_idx")
    @Field(name = "facility_languages")
    @Cascade(delete = true)
    private List<CodedType> languages = new ArrayList<>();

    @Order(column = "facility_contact_points_idx")
    @Field(name = "facility_contact_points")
    @Cascade(delete = true)
    private List<ContactPoint> contactPoints = new ArrayList<>();

    @Order(column = "facility_coded_types_idx")
    @Field(required = true, name = "facility_coded_types")
    @Cascade(delete = true)
    private List<CodedType> codedTypes = new ArrayList<>();

    @Order(column = "facility_extensions_idx")
    @Field(name = "facility_extensions")
    @Cascade(delete = true)
    private List<Extension> extensions = new ArrayList<>();

    @Field
    @Cascade(delete = true)
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

    @XmlElement
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

    @XmlElement
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
        return primaryName;
    }
}
