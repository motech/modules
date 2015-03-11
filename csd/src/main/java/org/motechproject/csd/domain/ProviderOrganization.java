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
 *     &lt;extension base="{urn:ihe:iti:csd:2013}uniqueID">
 *       &lt;sequence>
 *         &lt;element name="extension" type="{urn:ihe:iti:csd:2013}extension" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="address" type="{urn:ihe:iti:csd:2013}address" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="contactPoint" type="{urn:ihe:iti:csd:2013}contactPoint" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(propOrder = { "extensions", "addresses", "contactPoints" })
public class ProviderOrganization extends AbstractUniqueID {

    @Order(column = "provider_organization_extensions_idx")
    @Field(name = "provider_organization_extensions")
    private List<Extension> extensions = new ArrayList<>();

    @Order(column = "provider_organization_addresses_idx")
    @Field(name = "provider_organization_addresses")
    private List<Address> addresses = new ArrayList<>();

    @Order(column = "provider_organization_contact_points_idx")
    @Field(name = "provider_organization_contact_points")
    private List<ContactPoint> contactPoints = new ArrayList<>();

    public ProviderOrganization() {
    }

    public ProviderOrganization(String entityID) {
        setEntityID(entityID);
    }

    public ProviderOrganization(String entityID, List<Extension> extensions, List<Address> addresses, List<ContactPoint> contactPoints) {
        setEntityID(entityID);
        this.extensions = extensions;
        this.addresses = addresses;
        this.contactPoints = contactPoints;
    }

    public List<Extension> getExtensions() {
        return extensions;
    }

    @XmlElement(name = "extension")
    public void setExtensions(List<Extension> extensions) {
        this.extensions = extensions;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    @XmlElement(name = "address")
    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public List<ContactPoint> getContactPoints() {
        return contactPoints;
    }

    @XmlElement(name = "contactPoint")
    public void setContactPoints(List<ContactPoint> contactPoints) {
        this.contactPoints = contactPoints;
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

        ProviderOrganization that = (ProviderOrganization) o;

        if (addresses != null ? !addresses.equals(that.addresses) : that.addresses != null) {
            return false;
        }
        if (contactPoints != null ? !contactPoints.equals(that.contactPoints) : that.contactPoints != null) {
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
        result = 31 * result + (extensions != null ? extensions.hashCode() : 0);
        result = 31 * result + (addresses != null ? addresses.hashCode() : 0);
        result = 31 * result + (contactPoints != null ? contactPoints.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProviderOrganization{" +
                "extensions=" + extensions +
                ", addresses=" + addresses +
                ", contactPoints=" + contactPoints +
                '}';
    }
}
