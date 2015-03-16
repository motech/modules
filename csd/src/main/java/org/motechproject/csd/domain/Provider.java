package org.motechproject.csd.domain;

import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.FetchPlan;
import javax.jdo.annotations.Order;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Java class for provider complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="provider">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:ihe:iti:csd:2013}uniqueID">
 *       &lt;sequence>
 *         &lt;element name="otherID" type="{urn:ihe:iti:csd:2013}otherID" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="codedType" type="{urn:ihe:iti:csd:2013}codedtype" maxOccurs="unbounded"/>
 *         &lt;element name="demographic" type="{urn:ihe:iti:csd:2013}person"/>
 *         &lt;element name="language" type="{urn:ihe:iti:csd:2013}codedtype" maxOccurs="unbounded" minOccurs="0"/>
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
 *                             &lt;element name="extension" type="{urn:ihe:iti:csd:2013}extension" maxOccurs="unbounded" minOccurs="0"/>
 *                             &lt;element name="address" type="{urn:ihe:iti:csd:2013}address" maxOccurs="unbounded" minOccurs="0"/>
 *                             &lt;element name="contactPoint" type="{urn:ihe:iti:csd:2013}contactPoint" maxOccurs="unbounded" minOccurs="0"/>
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
 *         &lt;element name="facilities" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="facility" maxOccurs="unbounded">
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
 *                                       &lt;element name="organization" type="{urn:ihe:iti:csd:2013}uniqueID" minOccurs="0"/>
 *                                       &lt;element name="language" type="{urn:ihe:iti:csd:2013}codedtype" maxOccurs="unbounded" minOccurs="0"/>
 *                                       &lt;element name="operatingHours" type="{urn:ihe:iti:csd:2013}operatingHours" maxOccurs="unbounded" minOccurs="0"/>
 *                                       &lt;element name="freeBusyURI" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *                                       &lt;element name="extension" type="{urn:ihe:iti:csd:2013}extension" maxOccurs="unbounded" minOccurs="0"/>
 *                                     &lt;/sequence>
 *                                   &lt;/extension>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="operatingHours" type="{urn:ihe:iti:csd:2013}operatingHours" maxOccurs="unbounded" minOccurs="0"/>
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
 *         &lt;element name="credential" type="{urn:ihe:iti:csd:2013}credential" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="specialty" type="{urn:ihe:iti:csd:2013}codedtype" maxOccurs="unbounded" minOccurs="0"/>
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
@FetchPlan(maxFetchDepth = 6)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(propOrder = { "otherIDs", "codedTypes", "demographic", "languages", "providerOrganizations", "providerFacilities", "credentials", "specialties", "extensions", "record" })
public class Provider extends AbstractUniqueID {

    @Order(column = "provider_other_ids_idx")
    @Field(name = "provider_other_ids")
    @Cascade(delete = true)
    private List<OtherID> otherIDs = new ArrayList<>();

    @Field
    @Cascade(delete = true)
    private Person demographic;

    @Order(column = "provider_languages_idx")
    @Field(name = "provider_languages")
    @Cascade(delete = true)
    private List<CodedType> languages = new ArrayList<>();

    @Field
    @Cascade(delete = true)
    private ProviderOrganizations providerOrganizations;

    @Field
    @Cascade(delete = true)
    private ProviderFacilities providerFacilities;

    @Order(column = "provider_credentials_idx")
    @Field(name = "provider_credentials")
    @Cascade(delete = true)
    private List<Credential> credentials = new ArrayList<>();

    @Order(column = "provider_specialities_idx")
    @Field(name = "provider_specialities")
    @Cascade(delete = true)
    private List<CodedType> specialties = new ArrayList<>();

    @Order(column = "provider_coded_types_idx")
    @Field(required = true, name = "provider_coded_types")
    @Cascade(delete = true)
    private List<CodedType> codedTypes = new ArrayList<>();

    @Order(column = "provider_extensions_idx")
    @Field(name = "provider_extensions")
    @Cascade(delete = true)
    private List<Extension> extensions = new ArrayList<>();

    @Field
    @Cascade(delete = true)
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

    @XmlElement(name = "otherID")
    public void setOtherIDs(List<OtherID> otherIDs) {
        this.otherIDs = otherIDs;
    }

    public Person getDemographic() {
        return demographic;
    }

    @XmlElement(required = true)
    public void setDemographic(Person demographic) {
        this.demographic = demographic;
    }

    public List<CodedType> getLanguages() {
        return languages;
    }

    @XmlElement(name = "language")
    public void setLanguages(List<CodedType> languages) {
        this.languages = languages;
    }

    public ProviderOrganizations getProviderOrganizations() {
        return providerOrganizations;
    }

    @XmlElement(name = "organizations")
    public void setProviderOrganizations(ProviderOrganizations providerOrganizations) {
        this.providerOrganizations = providerOrganizations;
    }

    public ProviderFacilities getProviderFacilities() {
        return providerFacilities;
    }

    @XmlElement(name = "facilities")
    public void setProviderFacilities(ProviderFacilities providerFacilities) {
        this.providerFacilities = providerFacilities;
    }

    public List<Credential> getCredentials() {
        return credentials;
    }

    @XmlElement(name = "credential")
    public void setCredentials(List<Credential> credentials) {
        this.credentials = credentials;
    }

    public List<CodedType> getSpecialties() {
        return specialties;
    }

    @XmlElement(name = "specialty")
    public void setSpecialties(List<CodedType> specialties) {
        this.specialties = specialties;
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
