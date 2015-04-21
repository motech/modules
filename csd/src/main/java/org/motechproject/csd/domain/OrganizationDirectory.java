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
 *         &lt;element name="organization" type="{urn:ihe:iti:csd:2013}organization" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@Entity(maxFetchDepth = 5)
@XmlType
@XmlAccessorType(XmlAccessType.NONE)
public class OrganizationDirectory extends AbstractID {

    @UIDisplayable(position = 0)
    @Field(name = "organization_directory_organizations")
    @Cascade(delete = true)
    private Set<Organization> organizations = new HashSet<>();

    public OrganizationDirectory() {
    }

    public OrganizationDirectory(Set<Organization> organizations) {
        this.organizations = organizations;
    }

    public Set<Organization> getOrganizations() {
        return organizations;
    }

    @XmlElement(name = "organization")
    public void setOrganizations(Set<Organization> organizations) {
        this.organizations = organizations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrganizationDirectory that = (OrganizationDirectory) o;

        if (organizations != null ? !organizations.equals(that.organizations) : that.organizations != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return organizations != null ? organizations.hashCode() : 0;
    }

    @Override
    public String toString() {
        if (organizations != null && !organizations.isEmpty()) {
            return organizations.toString();
        }
        return "";
    }
}
