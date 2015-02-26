package org.motechproject.csd.domain;

import org.joda.time.DateTime;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.Order;
import java.util.List;

@Entity
public class Person {

    @Order(column = "person_names_idx")
    @Field(name = "person_names", required = true)
    private List<PersonName> names;

    @Order(column = "person_contact_points_idx")
    @Field(name = "person_contact_points")
    private List<ContactPoint> contactPoints;

    @Order(column = "person_addresses_idx")
    @Field(name = "person_addresses")
    private List<Address> addresses;

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

    public void setNames(List<PersonName> names) {
        this.names = names;
    }

    public List<ContactPoint> getContactPoints() {
        return contactPoints;
    }

    public void setContactPoints(List<ContactPoint> contactPoints) {
        this.contactPoints = contactPoints;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public DateTime getDateOfBirth() {
        return dateOfBirth;
    }

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
        if (dateOfBirth != null ? !dateOfBirth.equals(person.dateOfBirth) : person.dateOfBirth != null) {
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
