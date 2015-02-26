package org.motechproject.csd.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

@Entity
public class OrganizationContact {

    @Field
    private Provider provider;

    @Field
    private Person person;

    public OrganizationContact() {
    }

    public OrganizationContact(Provider provider) {
        this.provider = provider;
    }

    public OrganizationContact(Person person) {
        this.person = person;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
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
        if (provider != null ? !provider.equals(that.provider) : that.provider != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = provider != null ? provider.hashCode() : 0;
        result = 31 * result + (person != null ? person.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OrganizationContact{" +
                "provider=" + provider +
                ", person=" + person +
                '}';
    }
}
