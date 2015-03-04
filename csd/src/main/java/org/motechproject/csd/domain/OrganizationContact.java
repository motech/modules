package org.motechproject.csd.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.Ignore;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@Entity
@XmlType
public class OrganizationContact {

    @Field
    private String providerEntityID;

    @Field
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
        return contact;
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

        if (contact != null ? !contact.equals(that.contact) : that.contact != null) {
            return false;
        }
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
        result = 31 * result + (contact != null ? contact.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OrganizationContact{" +
                "providerEntityID='" + providerEntityID + '\'' +
                ", person=" + person +
                ", contact=" + contact +
                '}';
    }
}
