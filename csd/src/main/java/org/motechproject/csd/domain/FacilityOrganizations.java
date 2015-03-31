package org.motechproject.csd.domain;

import org.motechproject.mds.annotations.Cascade;
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
 *         &lt;element name="organization" maxOccurs="unbounded">
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
 *                             &lt;element name="language" type="{urn:ihe:iti:csd:2013}codedtype" maxOccurs="unbounded" minOccurs="0"/>
 *                             &lt;element name="operatingHours" type="{urn:ihe:iti:csd:2013}operatingHours" maxOccurs="unbounded" minOccurs="0"/>
 *                             &lt;element name="freeBusyURI" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *                             &lt;element name="extension" type="{urn:ihe:iti:csd:2013}extension" maxOccurs="unbounded" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/extension>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
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
public class FacilityOrganizations {

    @Order(column = "facility_organizations_organization_idx")
    @Field(required = true, name = "facility_organizations_organization")
    @Cascade(delete = true)
    private List<FacilityOrganization> facilityOrganizations = new ArrayList<>();

    public FacilityOrganizations() {
    }

    public FacilityOrganizations(List<FacilityOrganization> facilityOrganizations) {
        this.facilityOrganizations = facilityOrganizations;
    }

    public List<FacilityOrganization> getFacilityOrganizations() {
        return facilityOrganizations;
    }

    @XmlElement(name = "organization", required = true)
    public void setFacilityOrganizations(List<FacilityOrganization> facilityOrganizations) {
        this.facilityOrganizations = facilityOrganizations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FacilityOrganizations that = (FacilityOrganizations) o;

        if (!facilityOrganizations.equals(that.facilityOrganizations)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return facilityOrganizations.hashCode();
    }

    @Override
    public String toString() {
        if (facilityOrganizations != null && !facilityOrganizations.isEmpty()) {
            return facilityOrganizations.toString();
        }
        return "";
    }
}
