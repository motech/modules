package org.motechproject.csd.domain;

import org.joda.time.DateTime;
import org.motechproject.csd.adapters.DateAdapter;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.Order;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;

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
@Entity
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(propOrder = { "names", "contactPoints", "addresses", "gender", "dateOfBirth" })
public class Person {

    @Order(column = "person_names_idx")
    @Field(name = "person_names", required = true)
    private List<PersonName> names = new ArrayList<>();

    @Order(column = "person_contact_points_idx")
    @Field(name = "person_contact_points")
    private List<ContactPoint> contactPoints = new ArrayList<>();

    @Order(column = "person_addresses_idx")
    @Field(name = "person_addresses")
    private List<Address> addresses = new ArrayList<>();

    @Field
    private String gender;

    @Field
    private DateTime dateOfBirth;

    public Person() {
    }

    public Person(List<PersonName> names) {
        this.names = names;
    }

    public Person(List<PersonName> names, List<ContactPoint> contactPoints, List<Address> addresses, String gender, DateTime dateOfBirth) {
        this.names = names;
        this.contactPoints = contactPoints;
        this.addresses = addresses;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
    }

    public List<PersonName> getNames() {
        return names;
    }

    @XmlElement(name = "name", required = true)
    public void setNames(List<PersonName> names) {
        this.names = names;
    }

    public List<ContactPoint> getContactPoints() {
        return contactPoints;
    }

    @XmlElement(name = "contactPoint")
    public void setContactPoints(List<ContactPoint> contactPoints) {
        this.contactPoints = contactPoints;
    }

    @XmlElement(name = "address")
    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
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
        if (!names.equals(person.names)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = names.hashCode();
        result = 31 * result + (contactPoints != null ? contactPoints.hashCode() : 0);
        result = 31 * result + (addresses != null ? addresses.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (dateOfBirth != null ? dateOfBirth.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Person{" +
                "names=" + names +
                ", contactPoints=" + contactPoints +
                ", addresses=" + addresses +
                ", gender='" + gender + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }
}
