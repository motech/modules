package org.motechproject.csd.domain;

import org.motechproject.csd.constants.CSDConstants;
import org.motechproject.mds.annotations.Access;
import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.Ignore;
import org.motechproject.mds.annotations.UIDisplayable;
import org.motechproject.mds.util.SecurityMode;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
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
@XmlType(propOrder = { "otherIDs", "codedTypes", "demographic", "languages", "providerOrganizationSet", "providerFacilitySet", "credentials", "specialties", "extensions", "record" })
@Access(value = SecurityMode.PERMISSIONS, members = {CSDConstants.MANAGE_CSD})
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
    private Set<ProviderOrganization> providerOrganizations = new HashSet<>();

    @Field
    @Cascade(delete = true)
    private Set<ProviderFacility> providerFacilities = new HashSet<>();

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
                    Person demographic, Set<CodedType> languages, Set<ProviderOrganization> providerOrganizations,
                    Set<ProviderFacility> providerFacilities, Set<Credential> credentials, Set<CodedType> specialties) {
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

    @XmlTransient
    public Set<ProviderOrganization> getProviderOrganizations() {
        return providerOrganizations;
    }

    public void setProviderOrganizations(Set<ProviderOrganization> providerOrganizations) {
        this.providerOrganizations = providerOrganizations;
    }

    @XmlTransient
    public Set<ProviderFacility> getProviderFacilities() {
        return providerFacilities;
    }

    public void setProviderFacilities(Set<ProviderFacility> providerFacilities) {
        this.providerFacilities = providerFacilities;
    }

    @Ignore
    public Set<ProviderOrganization> getProviderOrganizationSet() {
        if (providerOrganizations != null && providerOrganizations.isEmpty()) {
            return null;
        }
        return providerOrganizations;
    }

    @XmlElementWrapper(name = "organizations")
    @XmlElement(name = "organization", required = true)
    public void setProviderOrganizationSet(Set<ProviderOrganization> providerOrganizationSet) {
        providerOrganizations = providerOrganizationSet;
    }

    @Ignore
    public Set<ProviderFacility> getProviderFacilitySet() {
        if (providerFacilities != null && providerFacilities.isEmpty()) {
            return null;
        }
        return providerFacilities;
    }

    @XmlElementWrapper(name = "facilities")
    @XmlElement(name = "facility", required = true)
    public void setProviderFacilitySet(Set<ProviderFacility> providerFacilitySet) {
        providerFacilities = providerFacilitySet;
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

    public Set<Extension> getExtensions() {
        return extensions;
    }

    @XmlElement(name = "extension")
    public void setExtensions(Set<Extension> extensions) {
        this.extensions = extensions;
    }

    public Set<CodedType> getCodedTypes() {
        return codedTypes;
    }

    @XmlElement(name = "codedType", required = true)
    public void setCodedTypes(Set<CodedType> codedTypes) {
        this.codedTypes = codedTypes;
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

        if (!demographic.equals(provider.demographic)) {
            return false;
        }
        if (!codedTypes.equals(provider.codedTypes)) {
            return false;
        }
        if (otherIDs != null ? !otherIDs.equals(provider.otherIDs) : provider.otherIDs != null) {
            return false;
        }
        if (languages != null ? !languages.equals(provider.languages) : provider.languages != null) {
            return false;
        }
        if (specialties != null ? !specialties.equals(provider.specialties) : provider.specialties != null) {
            return false;
        }
        if (credentials != null ? !credentials.equals(provider.credentials) : provider.credentials != null) {
            return false;
        }
        if (providerFacilities != null ? !providerFacilities.equals(provider.providerFacilities) : provider.providerFacilities != null) {
            return false;
        }
        if (providerOrganizations != null ? !providerOrganizations.equals(provider.providerOrganizations) : provider.providerOrganizations != null) {
            return false;
        }
        if (extensions != null ? !extensions.equals(provider.extensions) : provider.extensions != null) {
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
        return result;
    }

    @Override
    public String toString() {
        return demographic.toString();
    }
}
