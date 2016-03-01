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
        private String preferred;

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
        private String countryDistrict;

        @Expose
        private String address3;

        @Expose
        private String address4;

        @Expose
        private String address5;

        @Expose
        private String address6;

        @Expose
        private String startDate;

        @Expose
        private String endDate;

        @Expose
        private String latitude;

        @Expose
        private String longitude;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getPreferred() {
            return preferred;
        }

        public void setPreferred(String preferred) {
            this.preferred = preferred;
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

        public String getCountryDistrict() {
            return countryDistrict;
        }

        public void setCountryDistrict(String countryDistrict) {
            this.countryDistrict = countryDistrict;
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

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
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

        public String getFullAdressString() {
            StringBuilder address = new StringBuilder();
            return  address.append(address1).append(",").append(address2).append(",")
                    .append(cityVillage).append(",").append(stateProvince).append(",")
                    .append(country).append(",").append(postalCode).append(",")
                    .append(countryDistrict).append(",").append(address3).append(",")
                    .append(address4).append(",").append(address5).append(",")
                    .append(address6).append(",").append(startDate).append(",")
                    .append(endDate).append(",").append(latitude).append(",")
                    .append(longitude).append(",")
                    .toString();
        }

        @Override
        public int hashCode() {
            return Objects.hash(uuid, preferred, address1, address2, cityVillage, stateProvince, country, postalCode, countryDistrict, address3, address4, address5, address6, startDate, endDate, latitude, longitude);
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
            return  Objects.equals(this.uuid, other.uuid) && Objects.equals(this.preferred, other.preferred) &&
                    Objects.equals(this.address1, other.address1) && Objects.equals(this.address2, other.address2) &&
                    Objects.equals(this.cityVillage, other.cityVillage) && Objects.equals(this.stateProvince, other.stateProvince) &&
                    Objects.equals(this.country, other.country) && Objects.equals(this.postalCode, other.postalCode) &&
                    Objects.equals(this.countryDistrict, other.countryDistrict) && Objects.equals(this.address3, other.address3) &&
                    Objects.equals(this.address4, other.address4) && Objects.equals(this.address5, other.address5) &&
                    Objects.equals(this.address6, other.address6) && Objects.equals(this.startDate, other.startDate) &&
                    Objects.equals(this.endDate, other.endDate) && Objects.equals(this.latitude, other.latitude) &&
                    Objects.equals(this.longitude, other.longitude);
        }
    }

    /**
     * Represents audit info for that person
     */

    public static class AuditInfo {

        @Expose
        private Date dateCreated;

        @Expose
        private Date dateChanged;

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
}
