package org.motechproject.openmrs19.resource.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.Expose;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single person. This class stores personal information about the person. It's used by the
 * {@link Patient}. It's part of the OpenMRS model.
 */
public class Person {

    private String uuid;

    @Expose
    private String display;

    @Expose
    private String gender;

    @Expose
    private Integer age;

    @Expose
    private Date birthdate;

    @Expose
    private boolean birthdateEstimated;

    @Expose
    private boolean dead;

    @Expose
    private Concept causeOfDeath;

    @Expose
    private Date deathDate;

    @Expose
    private PreferredName preferredName;

    @Expose
    private List<PreferredName> names;

    @Expose
    private PreferredAddress preferredAddress;

    @Expose
    private List<PreferredAddress> addresses;

    @Expose
    private List<Attribute> attributes;

    @Expose
    private AuditInfo auditInfo;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public boolean isBirthdateEstimated() {
        return birthdateEstimated;
    }

    public void setBirthdateEstimated(boolean birthdateEstimated) {
        this.birthdateEstimated = birthdateEstimated;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public Date getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(Date deathDate) {
        this.deathDate = deathDate;
    }

    public Concept getCauseOfDeath() {
        return causeOfDeath;
    }

    public void setCauseOfDeath(Concept causeOfDeath) {
        this.causeOfDeath = causeOfDeath;
    }

    public PreferredName getPreferredName() {
        return preferredName;
    }

    public void setPreferredName(PreferredName preferredName) {
        this.preferredName = preferredName;
    }

    public PreferredAddress getPreferredAddress() {
        return preferredAddress;
    }

    public void setPreferredAddress(PreferredAddress preferredAddress) {
        this.preferredAddress = preferredAddress;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public List<PreferredName> getNames() {
        return names;
    }

    public void setNames(List<PreferredName> names) {
        this.names = names;
    }

    public List<PreferredAddress> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<PreferredAddress> addresses) {
        this.addresses = addresses;
    }

    public AuditInfo getAuditInfo() {
        return auditInfo;
    }

    public void setAuditInfo(AuditInfo auditInfo) {
        this.auditInfo = auditInfo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, display, gender, age, birthdate, birthdateEstimated, dead, causeOfDeath, deathDate, preferredName, names, preferredAddress, addresses, auditInfo, attributes);
    }

    /**
     * Implementation of the {@link JsonSerializer} interface for the {@link Person} class. It represents the person as
     * its ID.
     */
    public static class PersonSerializer implements JsonSerializer<Person> {

        @Override
        public JsonElement serialize(Person src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getUuid());
        }
    }

    /**
     * Represents a single preferred name. A preferred name will be displayed in search results and patient screens in
     * the OpenMRS.
     */
    public static class PreferredName {

        private String uuid;

        @Expose
        private String display;

        @Expose
        private String givenName;

        @Expose
        private String middleName;

        @Expose
        private String familyName;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getDisplay() {
            return display;
        }

        public void setDisplay(String display) {
            this.display = display;
        }

        public String getGivenName() {
            return givenName;
        }

        public void setGivenName(String givenName) {
            this.givenName = givenName;
        }

        public String getMiddleName() {
            return middleName;
        }

        public void setMiddleName(String middleName) {
            this.middleName = middleName;
        }

        public String getFamilyName() {
            return familyName;
        }

        public void setFamilyName(String familyName) {
            this.familyName = familyName;
        }

        @Override
        public int hashCode() {
            return Objects.hash(uuid, display, givenName, middleName, familyName);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final PreferredName other = (PreferredName) obj;
            return Objects.equals(this.uuid, other.uuid) &&
                    Objects.equals(this.display, other.display) &&
                    Objects.equals(this.givenName, other.givenName) &&
                    Objects.equals(this.middleName, other.middleName) &&
                    Objects.equals(this.familyName, other.familyName);
        }
    }

    /**
     * Represents a single preferred address.
     */
    public static class PreferredAddress {

        private String uuid;

        @Expose
        private String address1;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getAddress1() {
            return address1;
        }

        public void setAddress1(String address1) {
            this.address1 = address1;
        }

        @Override
        public int hashCode() {
            return Objects.hash(uuid, address1);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final PreferredAddress other = (PreferredAddress) obj;
            return Objects.equals(this.uuid, other.uuid) && Objects.equals(this.address1, other.address1);
        }
    }

    /**
     * Represents audit info for that person
     */

    public static class AuditInfo {

        private String uuid;

        @Expose
        private Date dateCreated;

        @Expose
        private Date dateChanged;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public Date getDateCreated() {
            return dateCreated;
        }

        public void setDateCreated(Date dateCreated) {
            this.dateCreated = dateCreated;
        }

        public Date getDateChanged() {
            return dateChanged;
        }

        public void setDateChanged(Date dateChanged) {
            this.dateChanged = dateChanged;
        }

        @Override
        public int hashCode() {
            return Objects.hash(uuid, dateCreated, dateChanged);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final AuditInfo other = (AuditInfo) obj;
            return Objects.equals(this.uuid, other.uuid) &&
                    Objects.equals(this.dateChanged, other.dateChanged) &&
                    Objects.equals(this.dateCreated, other.dateCreated);
        }
    }
}
