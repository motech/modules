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
 *                   &lt;element name="extension" type="{urn:ihe:iti:csd:2013}extension" maxOccurs="unbounded" minOccurs="0"/>
 *                   &lt;element name="address" type="{urn:ihe:iti:csd:2013}address" maxOccurs="unbounded" minOccurs="0"/>
 *                   &lt;element name="contactPoint" type="{urn:ihe:iti:csd:2013}contactPoint" maxOccurs="unbounded" minOccurs="0"/>
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
public class ProviderOrganizations {

    @Order(column = "provider_organizations_organization_idx")
    @Field(required = true, name = "provider_organizations_organization")
    @Cascade(delete = true)
    private List<ProviderOrganization> providerOrganizations = new ArrayList<>();

    public ProviderOrganizations() {
    }

    public ProviderOrganizations(List<ProviderOrganization> providerOrganizations) {
        this.providerOrganizations = providerOrganizations;
    }

    public List<ProviderOrganization> getProviderOrganizations() {
        return providerOrganizations;
    }

    @XmlElement(name = "organization", required = true)
    public void setProviderOrganizations(List<ProviderOrganization> providerOrganizations) {
        this.providerOrganizations = providerOrganizations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProviderOrganizations that = (ProviderOrganizations) o;

        if (!providerOrganizations.equals(that.providerOrganizations)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return providerOrganizations.hashCode();
    }

    @Override
    public String toString() {
        if (providerOrganizations != null && !providerOrganizations.isEmpty()) {
            return providerOrganizations.toString();
        }
        return "";
    }
}
