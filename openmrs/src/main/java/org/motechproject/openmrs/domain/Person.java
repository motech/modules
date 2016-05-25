package org.motechproject.openmrs.domain;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.Expose;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single person. This class stores personal information about the person. It's used by the
 * {@link Patient}.
 */
public class Person {

    private String uuid;
    private String display;

    private Name preferredName;
    @Expose
    private List<Name> names;

    private Address preferredAddress;
    @Expose
    private List<Address> addresses;

    @Expose
    private Date birthdate;
    @Expose
    private Boolean birthdateEstimated;
    @Expose
    private Integer age;
    @Expose
    private String gender;

    @Expose
    private Boolean dead;
    @Expose
    private Concept causeOfDeath;
    @Expose
    private Date deathDate;

    @Expose
    private List<Attribute> attributes = new ArrayList<Attribute>();

    private AuditInfo auditInfo;

    /**
     * Default constructor.
     */
    public Person() {
        this(null);
    }

    /**
     * Creates a person with the given OpenMRS {@code uuid}.
     *
     * @param uuid  the OpenMRS ID of the person
     */
    public Person(String uuid) {
        this.uuid = uuid;
    }

    /**
     * Represents a single address.
     */
    public static class Address {

        private String uuid;

        @Expose
        private String address1;

        @Expose
        private String address2;

        @Expose
        private String cityVillage;

        @Expose
        private String stateProvince;

        @Expose
        private String country;

        @Expose
        private String postalCode;

        @Expose
        private String countyDistrict;

        @Expose
        private String address3;

        @Expose
        private String address4;

        @Expose
        private String address5;

        @Expose
        private String address6;

        @Expose
        private Date startDate;

        @Expose
        private Date endDate;

        @Expose
        private String latitude;

        @Expose
        private String longitude;

        public Address() { }

        public Address(String address1, String address2, String address3, String address4, String address5, String address6,
                       String cityVillage, String stateProvince, String country, String postalCode, String countyDistrict,
                       String latitude, String longitude, Date startDate, Date endDate) {
            this.address1 = address1;
            this.address2 = address2;
            this.address3 = address3;
            this.address4 = address4;
            this.address5 = address5;
            this.address6 = address6;
            this.cityVillage = cityVillage;
            this.stateProvince = stateProvince;
            this.country = country;
            this.postalCode = postalCode;
            this.countyDistrict = countyDistrict;
            this.latitude = latitude;
            this.longitude = longitude;
            this.startDate = startDate;
            this.endDate = endDate;
        }

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

        public String getAddress2() {
            return address2;
        }

        public void setAddress2(String address2) {
            this.address2 = address2;
        }

        public String getCityVillage() {
            return cityVillage;
        }

        public void setCityVillage(String cityVillage) {
            this.cityVillage = cityVillage;
        }

        public String getStateProvince() {
            return stateProvince;
        }

        public void setStateProvince(String stateProvince) {
            this.stateProvince = stateProvince;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getPostalCode() {
            return postalCode;
        }

        public void setPostalCode(String postalCode) {
            this.postalCode = postalCode;
        }

        public String getCountyDistrict() {
            return countyDistrict;
        }

        public void setCountyDistrict(String countyDistrict) {
            this.countyDistrict = countyDistrict;
        }

        public String getAddress3() {
            return address3;
        }

        public void setAddress3(String address3) {
            this.address3 = address3;
        }

        public String getAddress4() {
            return address4;
        }

        public void setAddress4(String address4) {
            this.address4 = address4;
        }

        public String getAddress5() {
            return address5;
        }

        public void setAddress5(String address5) {
            this.address5 = address5;
        }

        public String getAddress6() {
            return address6;
        }

        public void setAddress6(String address6) {
            this.address6 = address6;
        }

        public Date getStartDate() {
            return startDate;
        }

        public void setStartDate(Date startDate) {
            this.startDate = startDate;
        }

        public Date getEndDate() {
            return endDate;
        }

        public void setEndDate(Date endDate) {
            this.endDate = endDate;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getFullAddressString() {
            StringBuilder address = new StringBuilder();
            return  address.append(address1).append(",").append(address2).append(",")
                    .append(cityVillage).append(",").append(stateProvince).append(",")
                    .append(country).append(",").append(postalCode).append(",")
                    .append(countyDistrict).append(",").append(address3).append(",")
                    .append(address4).append(",").append(address5).append(",")
                    .append(address6).append(",").append(startDate).append(",")
                    .append(endDate).append(",").append(latitude).append(",")
                    .append(longitude)
                    .toString();
        }

        @Override
        public int hashCode() {
            return Objects.hash(address1, address2, cityVillage, stateProvince, country, postalCode, countyDistrict, address3, address4, address5, address6, startDate, endDate, latitude, longitude);
        }

        @Override //NO CHECKSTYLE Cyclomatic Complexity
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final Address other = (Address) obj;

            return  Objects.equals(this.address1, other.address1) && Objects.equals(this.address2, other.address2) &&
                    Objects.equals(this.cityVillage, other.cityVillage) && Objects.equals(this.stateProvince, other.stateProvince) &&
                    Objects.equals(this.country, other.country) && Objects.equals(this.postalCode, other.postalCode) &&
                    Objects.equals(this.countyDistrict, other.countyDistrict) && Objects.equals(this.address3, other.address3) &&
                    Objects.equals(this.address4, other.address4) && Objects.equals(this.address5, other.address5) &&
                    Objects.equals(this.address6, other.address6) && Objects.equals(this.startDate, other.startDate) &&
                    Objects.equals(this.endDate, other.endDate) && Objects.equals(this.latitude, other.latitude) &&
                    Objects.equals(this.longitude, other.longitude);
        }
    }

    public static class Name {
        private String uuid;
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
            final Name other = (Name) obj;
            return Objects.equals(this.uuid, other.uuid) &&
                    Objects.equals(this.display, other.display) &&
                    Objects.equals(this.givenName, other.givenName) &&
                    Objects.equals(this.middleName, other.middleName) &&
                    Objects.equals(this.familyName, other.familyName);
        }
    }

    /**
     * Represents audit info for that person
     */

    public static class AuditInfo {

        private Date dateCreated;
        private Date dateChanged;

        //todo: MOTECH-2203
        public String getNewPerson() {
            if (dateChanged == null || dateChanged.equals(dateCreated)) {
                return "yes";
            }

            return "no";
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
            return Objects.hash(dateCreated, dateChanged);
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
            return Objects.equals(this.dateChanged, other.dateChanged)
                    && Objects.equals(this.dateCreated, other.dateCreated);
        }
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

    public static class PersonUpdateSerializer implements JsonSerializer<Person> {

        @Override
        public JsonElement serialize(Person src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject person = new JsonObject();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

            if (src.birthdate != null) {
                person.addProperty("birthdate", sdf.format(src.getBirthdate()));
            }
            if (src.birthdateEstimated != null) {
                person.addProperty("birthdateEstimated", src.getBirthdateEstimated());
            }
            if (src.gender != null) {
                person.addProperty("gender", src.getGender());
            }
            if (src.dead != null) {
                person.addProperty("dead", src.getDead());
            }
            if (src.causeOfDeath != null) {
                person.addProperty("causeOfDeath", src.getCauseOfDeath().getUuid());
            }

            return person;
        }
    }

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

    public Name getPreferredName() {
        return preferredName;
    }

    public void setPreferredName(Name preferredName) {
        this.preferredName = preferredName;
    }

    public List<Name> getNames() {
        return names;
    }

    public void setNames(List<Name> names) {
        this.names = names;
    }

    public Address getPreferredAddress() {
        return preferredAddress;
    }

    public void setPreferredAddress(Address preferredAddress) {
        this.preferredAddress = preferredAddress;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public Boolean getBirthdateEstimated() {
        return birthdateEstimated;
    }

    public void setBirthdateEstimated(Boolean birthdateEstimated) {
        this.birthdateEstimated = birthdateEstimated;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Boolean getDead() {
        return dead;
    }

    public void setDead(Boolean dead) {
        this.dead = dead;
    }

    public Concept getCauseOfDeath() {
        return causeOfDeath;
    }

    public void setCauseOfDeath(Concept causeOfDeath) {
        this.causeOfDeath = causeOfDeath;
    }

    public Date getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(Date deathDate) {
        this.deathDate = deathDate;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
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

    @Override //NO CHECKSTYLE Cyclomatic Complexity
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Person)) {
            return false;
        }

        Person other = (Person) o;

        return Objects.equals(this.uuid, other.uuid) && Objects.equals(this.display, other.display) && Objects.equals(this.gender, other.gender) &&
                Objects.equals(this.age, other.age) && Objects.equals(this.birthdate, other.birthdate) && Objects.equals(this.birthdateEstimated, other.birthdateEstimated) &&
                Objects.equals(this.dead, other.dead) && Objects.equals(this.causeOfDeath, other.causeOfDeath) && Objects.equals(this.deathDate, other.deathDate) &&
                Objects.equals(this.preferredName, other.preferredName) && Objects.equals(this.names, other.names) && Objects.equals(this.preferredAddress, other.preferredAddress) &&
                Objects.equals(this.addresses, other.addresses) && Objects.equals(this.auditInfo, other.auditInfo) && Objects.equals(this.attributes, other.attributes);

    }
}
