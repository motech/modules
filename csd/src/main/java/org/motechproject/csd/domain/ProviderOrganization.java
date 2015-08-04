package org.motechproject.csd.domain;

import org.motechproject.csd.constants.CSDConstants;
import org.motechproject.mds.annotations.Access;
import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.UIDisplayable;
import org.motechproject.mds.util.SecurityMode;

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
@Entity(maxFetchDepth = 2)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(propOrder = { "extensions", "addresses", "contactPoints" })
@Access(value = SecurityMode.PERMISSIONS, members = {CSDConstants.MANAGE_CSD})
public class ProviderOrganization extends AbstractUniqueID {

    @UIDisplayable(position = 2)
    @Field(name = "provider_organization_extensions", tooltip = "This is a locally defined extension for this entity.")
    @Cascade(delete = true)
    private Set<Extension> extensions = new HashSet<>();

    @UIDisplayable(position = 0)
    @Field(name = "provider_organization_addresses", tooltip = "The address(es) of this provider's organization, if known. " +
            "More than one address may be specified, but the primary address must be indicated as such.")
    @Cascade(delete = true)
    private Set<Address> addresses = new HashSet<>();

    @UIDisplayable(position = 1)
    @Field(name = "provider_organization_contact_points", tooltip = "This provider organization’s contact points " +
            "(i.e. Business Phone, Fax, Encryption Certificate, etc.).")
    @Cascade(delete = true)
    private Set<ContactPoint> contactPoints = new HashSet<>();

    public ProviderOrganization() {
    }

    public ProviderOrganization(String entityID) {
        setEntityID(entityID);
    }

    public ProviderOrganization(String entityID, Set<Extension> extensions, Set<Address> addresses, Set<ContactPoint> contactPoints) {
        setEntityID(entityID);
        this.extensions = extensions;
        this.addresses = addresses;
        this.contactPoints = contactPoints;
    }

    public Set<Extension> getExtensions() {
        return extensions;
    }

    @XmlElement(name = "extension")
    public void setExtensions(Set<Extension> extensions) {
        this.extensions = extensions;
    }

    public Set<Address> getAddresses() {
        return addresses;
    }

    @XmlElement(name = "address")
    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

    public Set<ContactPoint> getContactPoints() {
        return contactPoints;
    }

    @XmlElement(name = "contactPoint")
    public void setContactPoints(Set<ContactPoint> contactPoints) {
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
        result = 31 * result + (contactPoints != null ? contactPoints.hashCode() : 0);
        result = 31 * result + (addresses != null ? addresses.hashCode() : 0);
        result = 31 * result + (extensions != null ? extensions.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
