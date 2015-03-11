package org.motechproject.csd.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.Order;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="facility" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;extension base="{urn:ihe:iti:csd:2013}uniqueID">
 *                 &lt;sequence>
 *                   &lt;element name="service" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;extension base="{urn:ihe:iti:csd:2013}uniqueID">
 *                           &lt;sequence>
 *                             &lt;element name="name" type="{urn:ihe:iti:csd:2013}name" maxOccurs="unbounded" minOccurs="0"/>
 *                             &lt;element name="organization" type="{urn:ihe:iti:csd:2013}uniqueID" minOccurs="0"/>
 *                             &lt;element name="language" type="{urn:ihe:iti:csd:2013}codedtype" maxOccurs="unbounded" minOccurs="0"/>
 *                             &lt;element name="operatingHours" type="{urn:ihe:iti:csd:2013}operatingHours" maxOccurs="unbounded" minOccurs="0"/>
 *                             &lt;element name="freeBusyURI" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *                             &lt;element name="extension" type="{urn:ihe:iti:csd:2013}extension" maxOccurs="unbounded" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/extension>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="operatingHours" type="{urn:ihe:iti:csd:2013}operatingHours" maxOccurs="unbounded" minOccurs="0"/>
 *                   &lt;element name="extension" type="{urn:ihe:iti:csd:2013}extension" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/extension>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@Entity
@XmlType
@XmlAccessorType(XmlAccessType.NONE)
public class ProviderFacilities {

    @Order(column = "provider_facilities_facility_idx")
    @Field(required = true, name = "provider_facilities_facility")
    private List<ProviderFacility> providerFacilities = new ArrayList<>();

    public ProviderFacilities() {
    }

    public ProviderFacilities(List<ProviderFacility> providerFacilities) {
        this.providerFacilities = providerFacilities;
    }

    public List<ProviderFacility> getProviderFacilities() {
        return providerFacilities;
    }

    @XmlElement(name = "facility", required = true)
    public void setProviderFacilities(List<ProviderFacility> providerFacilities) {
        this.providerFacilities = providerFacilities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProviderFacilities that = (ProviderFacilities) o;

        if (!providerFacilities.equals(that.providerFacilities)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return providerFacilities.hashCode();
    }

    @Override
    public String toString() {
        return "ProviderFacilities{" +
                "providerFacilities=" + providerFacilities +
                '}';
    }
}
