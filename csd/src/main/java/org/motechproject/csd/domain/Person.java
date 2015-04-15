package org.motechproject.csd.domain;

import org.joda.time.DateTime;
import org.motechproject.csd.adapters.DateAdapter;
import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.UIDisplayable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>Java class for person complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="person">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;extension base="{urn:ihe:iti:csd:2013}name">
 *                 &lt;attribute ref="{http://www.w3.org/XML/1998/namespace}lang"/>
 *               &lt;/extension>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="contactPoint" type="{urn:ihe:iti:csd:2013}contactPoint" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="address" type="{urn:ihe:iti:csd:2013}address" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="gender" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dateOfBirth" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@Entity(maxFetchDepth = 2)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(propOrder = { "names", "contactPoints", "addresses", "gender", "dateOfBirth" })
public class Person {

    @UIDisplayable(position = 0)
    @Field(name = "person_names", required = true)
    @Cascade(delete = true)
    private Set<PersonName> names = new HashSet<>();

    @UIDisplayable(position = 1)
    @Field(name = "person_contact_points")
    @Cascade(delete = true)
    private Set<ContactPoint> contactPoints = new HashSet<>();

    @UIDisplayable(position = 2)
    @Field(name = "person_addresses")
    @Cascade(delete = true)
    private Set<Address> addresses = new HashSet<>();

    @UIDisplayable(position = 3)
    @Field
    private String gender;

    @UIDisplayable(position = 4)
    @Field
    private DateTime dateOfBirth;

    public Person() {
    }

    public Person(Set<PersonName> names) {
        this.names = names;
    }

    public Person(Set<PersonName> names, Set<ContactPoint> contactPoints, Set<Address> addresses, String gender, DateTime dateOfBirth) {
        this.names = names;
        this.contactPoints = contactPoints;
        this.addresses = addresses;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
    }

    public Set<PersonName> getNames() {
        return names;
    }

    @XmlElement(name = "name", required = true)
    public void setNames(Set<PersonName> names) {
        this.names = names;
    }

    public Set<ContactPoint> getContactPoints() {
        return contactPoints;
    }

    @XmlElement(name = "contactPoint")
    public void setContactPoints(Set<ContactPoint> contactPoints) {
        this.contactPoints = contactPoints;
    }

    @XmlElement(name = "address")
    public Set<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

    public String getGender() {
        return gender;
    }

    @XmlElement
    public void setGender(String gender) {
        this.gender = gender;
    }

    public DateTime getDateOfBirth() {
        return dateOfBirth;
    }

    @XmlElement
    @XmlSchemaType(name = "date")
    @XmlJavaTypeAdapter(type = DateTime.class, value = DateAdapter.class)
    public void setDateOfBirth(DateTime dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override //NO CHECKSTYLE CyclomaticComplexity
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Person person = (Person) o;

        if (!names.equals(person.names)) {
            return false;
        }
        if (addresses != null ? !addresses.equals(person.addresses) : person.addresses != null) {
            return false;
        }
        if (contactPoints != null ? !contactPoints.equals(person.contactPoints) : person.contactPoints != null) {
            return false;
        }
        if (dateOfBirth != null ? !dateOfBirth.toLocalDate().isEqual(person.dateOfBirth.toLocalDate()) : person.dateOfBirth != null) {
            return false;
        }
        if (gender != null ? !gender.equals(person.gender) : person.gender != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = names.hashCode();
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (dateOfBirth != null ? dateOfBirth.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return names.toString();
    }
}
