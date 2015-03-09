package org.motechproject.csd.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.Order;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
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
 *         &lt;element name="facility" type="{urn:ihe:iti:csd:2013}facility" maxOccurs="unbounded" minOccurs="0"/>
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
public class FacilityDirectory {

    @Order(column = "service_directory_services_idx")
    @Field(name = "facility_directory_facilities")
    private List<Facility> facilities;

    public FacilityDirectory() {
    }

    public FacilityDirectory(List<Facility> facilities) {
        this.facilities = facilities;
    }

    public List<Facility> getFacilities() {
        return facilities;
    }

    @XmlElement(name = "facility")
    public void setFacilities(List<Facility> facilities) {
        this.facilities = facilities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FacilityDirectory that = (FacilityDirectory) o;

        if (facilities != null ? !facilities.equals(that.facilities) : that.facilities != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return facilities != null ? facilities.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "FacilityDirectory{" +
                "facilities=" + facilities +
                '}';
    }
}
