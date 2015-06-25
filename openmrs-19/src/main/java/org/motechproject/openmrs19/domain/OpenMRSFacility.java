package org.motechproject.openmrs19.domain;

import java.util.Objects;

/**
 * Represents a single facility. A facility is a physical place where patients can be seen. It's a part of the MOTECH
 * model.
 */
public class OpenMRSFacility {

    private String id;
    private String name;
    private String country;
    private String region;
    private String countyDistrict;
    private String stateProvince;

    /**
     * Creates a facility with the given {@code id}.
     *
     * @param id  the ID of the facility
     */
    public OpenMRSFacility(String id) {
        this(id, null, null, null, null, null);
    }

    /**
     * Creates a facility with the given {@code name} that is placed in the given {@code country}, {@code region},
     * {@code countryDistrict} and {@code stateProvince}.
     *
     * @param name  the name of the facility
     * @param country  the name of the country where the facility is placed
     * @param region  the name of the region where the facility is placed
     * @param countyDistrict  the name of the county/district where the facility is placed
     * @param stateProvince  the name of the state/province where the facility is placed
     */
    public OpenMRSFacility(String name, String country, String region, String countyDistrict, String stateProvince) {
        this(null, name, country, region, countyDistrict, stateProvince);
    }

    /**
     * Creates a facility with the given {@code name} and {@code id} that is placed in the given {@code country},
     * {@code region}, {@code countryDistrict} and {@code stateProvince}.
     *
     * @param id  the ID of the facility
     * @param name  the name of the facility
     * @param country  the name of the country where the facility is placed
     * @param region  the name of the region where the facility is placed
     * @param countyDistrict  the name of the county/district where the facility is placed
     * @param stateProvince  the name of the state/province where the facility is placed
     */
    public OpenMRSFacility(String id, String name, String country, String region, String countyDistrict, String stateProvince) {
        this.name = name;
        this.country = country;
        this.region = region;
        this.countyDistrict = countyDistrict;
        this.stateProvince = stateProvince;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getRegion() {
        return region;
    }

    public String getCountyDistrict() {
        return countyDistrict;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    @Deprecated
    public String getId() {
        return id;
    }

    @Deprecated
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setCountyDistrict(String countyDistrict) {
        this.countyDistrict = countyDistrict;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    public String getFacilityId() {
        return id;
    }

    public void setFacilityId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OpenMRSFacility)) {
            return false;
        }

        OpenMRSFacility facility = (OpenMRSFacility) o;

        return Objects.equals(country, facility.country) && Objects.equals(countyDistrict, facility.countyDistrict) &&
                Objects.equals(id, facility.id) && Objects.equals(name, facility.name) &&
                Objects.equals(region, facility.region) && Objects.equals(stateProvince, facility.stateProvince);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (region != null ? region.hashCode() : 0);
        result = 31 * result + (countyDistrict != null ? countyDistrict.hashCode() : 0);
        result = 31 * result + (stateProvince != null ? stateProvince.hashCode() : 0);
        return result;
    }

}
