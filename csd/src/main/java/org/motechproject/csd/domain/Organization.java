package org.motechproject.csd.domain;

import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.Ignore;

import javax.jdo.annotations.Order;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Java class for organization complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="organization">
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
 *         &lt;element name="credential" type="{urn:ihe:iti:csd:2013}credential" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="language" type="{urn:ihe:iti:csd:2013}codedtype" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="specialization" type="{urn:ihe:iti:csd:2013}codedtype" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="contactPoint" type="{urn:ihe:iti:csd:2013}contactPoint" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="parent" type="{urn:ihe:iti:csd:2013}uniqueID" minOccurs="0"/>
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
@XmlType(propOrder = { "otherIDs", "codedTypes", "primaryName", "otherNames", "addresses", "contacts", "credentials", "languages", "specializations", "contactPoints", "parent", "extensions", "record" })
public class Organization extends AbstractUniqueID {

    @Order(column = "organization_other_ids_idx")
    @Field(name = "organization_other_ids")
    @Cascade(delete = true)
    private List<OtherID> otherIDs = new ArrayList<>();

    @Field(required = true)
    private String primaryName;

    @Order(column = "organization_other_names_idx")
    @Field(name = "organization_other_names")
    @Cascade(delete = true)
    private List<OtherName> otherNames = new ArrayList<>();

    @Order(column = "organization_addresses_idx")
    @Field(name = "organization_addresses")
    @Cascade(delete = true)
    private List<Address> addresses = new ArrayList<>();

    @Order(column = "organization_contacts_idx")
    @Field(name = "organization_contacts")
    @Cascade(delete = true)
    private List<OrganizationContact> contacts = new ArrayList<>();

    @Order(column = "organization_credentials_idx")
    @Field(name = "organization_credentials")
    @Cascade(delete = true)
    private List<Credential> credentials = new ArrayList<>();

    @Order(column = "organization_languages_idx")
    @Field(name = "organization_languages")
    @Cascade(delete = true)
    private List<CodedType> languages = new ArrayList<>();

    @Order(column = "organization_specialization_idx")
    @Field(name = "organization_specialization")
    @Cascade(delete = true)
    private List<CodedType> specializations = new ArrayList<>();

    @Order(column = "organization_contact_points_idx")
    @Field(name = "organization_contact_points")
    @Cascade(delete = true)
    private List<ContactPoint> contactPoints = new ArrayList<>();

    @Order(column = "organization_coded_types_idx")
    @Field(required = true, name = "organization_coded_types")
    @Cascade(delete = true)
    private List<CodedType> codedTypes = new ArrayList<>();

    @Field
    private String parentOrganization;

    @Ignore
    private UniqueID parent;

    @Order(column = "organization_extensions_idx")
    @Field(name = "organization_extensions")
    @Cascade(delete = true)
    private List<Extension> extensions = new ArrayList<>();

    @Field
    @Cascade(delete = true)
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
        if (parent == null) {
            if (parentOrganization != null && !parentOrganization.isEmpty()) {
                parent = new UniqueID(parentOrganization);
            }
        }
        return parent;
    }

    @XmlElement
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
        return primaryName;
    }
}
