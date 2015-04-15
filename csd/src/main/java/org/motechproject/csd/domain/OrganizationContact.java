package org.motechproject.csd.domain;

import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.Ignore;
import org.motechproject.mds.annotations.UIDisplayable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="provider" type="{urn:ihe:iti:csd:2013}uniqueID"/>
 *         &lt;element name="person" type="{urn:ihe:iti:csd:2013}person"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@Entity(maxFetchDepth = 3)
@XmlType
@XmlAccessorType(XmlAccessType.NONE)
public class OrganizationContact {

    @UIDisplayable(position = 1)
    @Field
    private String providerEntityID;

    @UIDisplayable(position = 0)
    @Field
    @Cascade(delete = true)
    private Person person;

    @Ignore
    private Object contact;

    public OrganizationContact() {
    }

    public OrganizationContact(String providerEntityID) {
        this.providerEntityID = providerEntityID;
        contact = new UniqueID(providerEntityID);
    }

    public OrganizationContact(Person person) {
        this.person = person;
        contact = person;
    }

    @XmlTransient
    public String getProviderEntityID() {
        return providerEntityID;
    }

    public void setProviderEntityID(String providerEntityID) {
        this.providerEntityID = providerEntityID;
        contact = new UniqueID(providerEntityID);
    }

    @XmlTransient
    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
        contact = person;
    }

    @XmlElements(value = {
            @XmlElement(name = "provider", type = UniqueID.class),
            @XmlElement(name = "person", type = Person.class),
    })
    public Object getContact() {
        if (contact != null) {
            return contact;
        } else {
            if (providerEntityID != null && !providerEntityID.isEmpty()) {
                return new UniqueID(providerEntityID);
            } else {
                return person;
            }
        }
    }

    public void setContact(Object contact) {
        this.contact = contact;
        if (contact instanceof Person) {
            person = (Person) contact;
        } else if (contact instanceof UniqueID) {
            providerEntityID = ((UniqueID) contact).getEntityID();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrganizationContact that = (OrganizationContact) o;

        if (person != null ? !person.equals(that.person) : that.person != null) {
            return false;
        }
        if (providerEntityID != null ? !providerEntityID.equals(that.providerEntityID) : that.providerEntityID != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = providerEntityID != null ? providerEntityID.hashCode() : 0;
        result = 31 * result + (person != null ? person.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        if (providerEntityID != null && !providerEntityID.isEmpty()) {
            return providerEntityID;
        } else {
            return person.toString();
        }
    }
}
