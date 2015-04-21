package org.motechproject.csd.domain;

import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.UIDisplayable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.HashSet;
import java.util.Set;

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
@Entity(maxFetchDepth = 4)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(propOrder = { "otherIDs", "codedTypes", "demographic", "languages", "providerOrganizations", "providerFacilities", "credentials", "specialties", "extensions", "record" })
public class Provider extends BaseMainEntity {

    @Field(name = "provider_other_ids")
    @Cascade(delete = true)
    private Set<OtherID> otherIDs = new HashSet<>();

    @UIDisplayable(position = 0)
    @Field(required = true)
    @Cascade(delete = true)
    private Person demographic;

    @UIDisplayable(position = 1)
    @Field(name = "provider_languages")
    @Cascade(delete = true)
    private Set<CodedType> languages = new HashSet<>();

    @Field
    @Cascade(delete = true)
    private ProviderOrganizations providerOrganizations;

    @Field
    @Cascade(delete = true)
    private ProviderFacilities providerFacilities;

    @Field(name = "provider_credentials")
    @Cascade(delete = true)
    private Set<Credential> credentials = new HashSet<>();

    @UIDisplayable(position = 2)
    @Field(name = "provider_specialities")
    @Cascade(delete = true)
    private Set<CodedType> specialties = new HashSet<>();

    @UIDisplayable(position = 3)
    @Field(required = true, name = "provider_coded_types")
    @Cascade(delete = true)
    private Set<CodedType> codedTypes = new HashSet<>();

    @Field(name = "provider_extensions")
    @Cascade(delete = true)
    private Set<Extension> extensions = new HashSet<>();

    public Provider() {
    }

    public Provider(String entityID, Set<CodedType> codedTypes, Record record, Person demographic) {
        setEntityID(entityID);
        this.codedTypes = codedTypes;
        setRecord(record);
        this.demographic = demographic;
    }

    public Provider(String entityID, Set<CodedType> codedTypes, Set<Extension> extensions, Record record, Set<OtherID> otherIDs, //NO CHECKSTYLE ArgumentCount
                    Person demographic, Set<CodedType> languages, ProviderOrganizations providerOrganizations,
                    ProviderFacilities providerFacilities, Set<Credential> credentials, Set<CodedType> specialties) {
        setEntityID(entityID);
        this.codedTypes = codedTypes;
        this.extensions = extensions;
        setRecord(record);
        this.otherIDs = otherIDs;
        this.demographic = demographic;
        this.languages = languages;
        this.providerOrganizations = providerOrganizations;
        this.providerFacilities = providerFacilities;
        this.credentials = credentials;
        this.specialties = specialties;
    }

    public Set<OtherID> getOtherIDs() {
        return otherIDs;
    }

    @XmlElement(name = "otherID")
    public void setOtherIDs(Set<OtherID> otherIDs) {
        this.otherIDs = otherIDs;
    }

    public Person getDemographic() {
        return demographic;
    }

    @XmlElement(required = true)
    public void setDemographic(Person demographic) {
        this.demographic = demographic;
    }

    public Set<CodedType> getLanguages() {
        return languages;
    }

    @XmlElement(name = "language")
    public void setLanguages(Set<CodedType> languages) {
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

    public Set<Credential> getCredentials() {
        return credentials;
    }

    @XmlElement(name = "credential")
    public void setCredentials(Set<Credential> credentials) {
        this.credentials = credentials;
    }

    public Set<CodedType> getSpecialties() {
        return specialties;
    }

    @XmlElement(name = "specialty")
    public void setSpecialties(Set<CodedType> specialties) {
        this.specialties = specialties;
    }

    public Set<CodedType> getCodedTypes() {
        return codedTypes;
    }

    @XmlElement(name = "codedType", required = true)
    public void setCodedTypes(Set<CodedType> codedTypes) {
        this.codedTypes = codedTypes;
    }

    public Set<Extension> getExtensions() {
        return extensions;
    }

    @XmlElement(name = "extension")
    public void setExtensions(Set<Extension> extensions) {
        this.extensions = extensions;
    }

    @Override
    public String toString() {
        return demographic.toString();
    }
}
