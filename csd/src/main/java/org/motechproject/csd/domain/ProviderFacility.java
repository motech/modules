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
 *                   &lt;element name="organization" type="{urn:ihe:iti:csd:2013}uniqueID" minOccurs="0"/>
 *                   &lt;element name="language" type="{urn:ihe:iti:csd:2013}codedtype" maxOccurs="unbounded" minOccurs="0"/>
 *                   &lt;element name="operatingHours" type="{urn:ihe:iti:csd:2013}operatingHours" maxOccurs="unbounded" minOccurs="0"/>
 *                   &lt;element name="freeBusyURI" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *                   &lt;element name="extension" type="{urn:ihe:iti:csd:2013}extension" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/extension>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="operatingHours" type="{urn:ihe:iti:csd:2013}operatingHours" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(propOrder = { "services", "operatingHours", "extensions" })
public class ProviderFacility extends AbstractUniqueID {

    @UIDisplayable(position = 0)
    @Field(name = "provider_facility_services")
    @Cascade(delete = true)
    private Set<ProviderFacilityService> services = new HashSet<>();

    @UIDisplayable(position = 1)
    @Field(name = "provider_facility_operating_hours")
    @Cascade(delete = true)
    private Set<OperatingHours> operatingHours = new HashSet<>();

    @UIDisplayable(position = 2)
    @Field(name = "provider_facility_extensions")
    @Cascade(delete = true)
    private Set<Extension> extensions = new HashSet<>();

    public ProviderFacility() {
    }

    public ProviderFacility(String entityID) {
        setEntityID(entityID);
    }

    public ProviderFacility(String entityID, Set<ProviderFacilityService> services, Set<OperatingHours> operatingHours,
                            Set<Extension> extensions) {
        setEntityID(entityID);
        this.services = services;
        this.operatingHours = operatingHours;
        this.extensions = extensions;
    }

    public Set<ProviderFacilityService> getServices() {
        return services;
    }

    @XmlElement(name = "service")
    public void setServices(Set<ProviderFacilityService> services) {
        this.services = services;
    }

    public Set<OperatingHours> getOperatingHours() {
        return operatingHours;
    }

    @XmlElement
    public void setOperatingHours(Set<OperatingHours> operatingHours) {
        this.operatingHours = operatingHours;
    }

    public Set<Extension> getExtensions() {
        return extensions;
    }

    @XmlElement(name = "extension")
    public void setExtensions(Set<Extension> extensions) {
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

        ProviderFacility that = (ProviderFacility) o;

        if (operatingHours != null ? !operatingHours.equals(that.operatingHours) : that.operatingHours != null) {
            return false;
        }
        if (services != null ? !services.equals(that.services) : that.services != null) {
            return false;
        }
        if (extensions != null ? !extensions.equals(that.extensions) : that.extensions != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (services != null ? services.hashCode() : 0);
        result = 31 * result + (operatingHours != null ? operatingHours.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
