package org.motechproject.openmrs.domain;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.Expose;

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * Represents a single location. A location is a physical place where patients can be seen.
 */
public class Location {

    private String uuid;
    private String display;

    @Expose
    private String name;
    @Expose
    private String country;
    @Expose
    private String address6;
    @Expose
    private String countyDistrict;
    @Expose
    private String stateProvince;
    @Expose
    private String description;

    public Location() {
        this(null);
    }

    /**
     * Creates a location with the given {@code uuid}.
     *
     * @param uuid  the ID of the location
     */
    public Location(String uuid) {
        this(uuid, null, null, null, null, null, null);
    }

    /**
     * Creates a location with the given {@code name} that is placed in the given {@code country}, {@code address6},
     * {@code countryDistrict} and {@code stateProvince}.
     *
     * @param name  the name of the location
     * @param country  the name of the country where the location is placed
     * @param address6  the name of the region where the location is placed
     * @param countyDistrict  the name of the county/district where the location is placed
     * @param stateProvince  the name of the state/province where the location is placed
     */
    public Location(String name, String country, String address6, String countyDistrict, String stateProvince) {
        this(null, null, name, country, address6, countyDistrict, stateProvince);
    }

    /**
     * Creates a location with the given {@code name}, {@code display} and {@code uuid} that is placed in the given {@code country},
     * {@code address6}, {@code countryDistrict} and {@code stateProvince}.
     *
     * @param uuid  the ID of the location
     * @param display  the display of the location
     * @param name  the name of the location
     * @param country  the name of the country where the location is placed
     * @param address6  the name of the region where the location is placed
     * @param countyDistrict  the name of the county/district where the location is placed
     * @param stateProvince  the name of the state/province where the location is placed
     */
    public Location(String uuid, String display, String name, String country, String address6, String countyDistrict, String stateProvince) {
        this.uuid = uuid;
        this.display = display;
        this.name = name;
        this.country = country;
        this.address6 = address6;
        this.countyDistrict = countyDistrict;
        this.stateProvince = stateProvince;
    }

    /**
     * Implementation of the {@link JsonSerializer} interface for the {@link Location} class. It represents the location
     * as its ID.
     */
    public static class LocationSerializer implements JsonSerializer<Location> {
        @Override
        public JsonElement serialize(Location src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getUuid());
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress6() {
        return address6;
    }

    public void setAddress6(String address6) {
        this.address6 = address6;
    }

    public String getCountyDistrict() {
        return countyDistrict;
    }

    public void setCountyDistrict(String countyDistrict) {
        this.countyDistrict = countyDistrict;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, display, name, stateProvince, country, countyDistrict, address6, description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Location)) {
            return false;
        }

        Location other = (Location) o;

        return Objects.equals(this.uuid, other.uuid) && Objects.equals(this.display, other.display) &&
                Objects.equals(this.name, other.name) && Objects.equals(this.stateProvince, other.stateProvince) &&
                Objects.equals(this.country, other.country) && Objects.equals(this.countyDistrict, other.countyDistrict) &&
                Objects.equals(this.address6, other.address6) && Objects.equals(this.description, other.description);
    }
}
