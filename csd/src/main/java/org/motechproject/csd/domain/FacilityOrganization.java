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
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;extension base="{urn:ihe:iti:csd:2013}uniqueID">
 *       &lt;sequence>
 *         &lt;element name="service" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;extension base="{urn:ihe:iti:csd:2013}uniqueID">
 *                 &lt;sequence>
 *                   &lt;element name="name" type="{urn:ihe:iti:csd:2013}name" maxOccurs="unbounded" minOccurs="0"/>
 *                   &lt;element name="language" type="{urn:ihe:iti:csd:2013}codedtype" maxOccurs="unbounded" minOccurs="0"/>
 *                   &lt;element name="operatingHours" type="{urn:ihe:iti:csd:2013}operatingHours" maxOccurs="unbounded" minOccurs="0"/>
 *                   &lt;element name="freeBusyURI" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *                   &lt;element name="extension" type="{urn:ihe:iti:csd:2013}extension" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/extension>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="extension" type="{urn:ihe:iti:csd:2013}extension" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(propOrder = { "services", "extensions" })
public class FacilityOrganization extends AbstractUniqueID {

    @UIDisplayable(position = 0)
    @Order(column = "facility_organization_services_idx")
    @Field(name = "facility_organization_services")
    @Cascade(delete = true)
    private List<FacilityOrganizationService> services = new ArrayList<>();

    @UIDisplayable(position = 1)
    @Order(column = "facility_organization_extensions_idx")
    @Field(name = "facility_organization_extensions")
    @Cascade(delete = true)
    private List<Extension> extensions = new ArrayList<>();

    public FacilityOrganization() {
    }

    public FacilityOrganization(String entityID) {
        setEntityID(entityID);
    }

    public FacilityOrganization(String entityID, List<FacilityOrganizationService> services, List<Extension> extensions) {
        setEntityID(entityID);
        this.services = services;
        this.extensions = extensions;
    }

    public List<FacilityOrganizationService> getServices() {
        return services;
    }

    @XmlElement(name = "service")
    public void setServices(List<FacilityOrganizationService> services) {
        this.services = services;
    }

    public List<Extension> getExtensions() {
        return extensions;
    }

    @XmlElement(name = "extension")
    public void setExtensions(List<Extension> extensions) {
        this.extensions = extensions;
    }

    @Override
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

        FacilityOrganization that = (FacilityOrganization) o;

        if (extensions != null ? !extensions.equals(that.extensions) : that.extensions != null) {
            return false;
        }
        if (services != null ? !services.equals(that.services) : that.services != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (services != null ? services.hashCode() : 0);
        result = 31 * result + (extensions != null ? extensions.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
