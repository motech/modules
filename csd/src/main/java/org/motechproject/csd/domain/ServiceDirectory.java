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
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="service" type="{urn:ihe:iti:csd:2013}service" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@Entity(maxFetchDepth = 2)
@XmlType
@XmlAccessorType(XmlAccessType.NONE)
public class ServiceDirectory extends AbstractID {

    @UIDisplayable(position = 0)
    @Field(name = "service_directory_services")
    @Cascade(delete = true)
    private Set<Service> services = new HashSet<>();

    public ServiceDirectory() {
    }

    public ServiceDirectory(Set<Service> services) {
        this.services = services;
    }

    public Set<Service> getServices() {
        return services;
    }

    @XmlElement(name = "service")
    public void setServices(Set<Service> services) {
        this.services = services;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ServiceDirectory that = (ServiceDirectory) o;

        if (services != null ? !services.equals(that.services) : that.services != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return services != null ? services.hashCode() : 0;
    }

    @Override
    public String toString() {
        if (services != null && !services.isEmpty()) {
            return services.toString();
        }
        return "";
    }
}
