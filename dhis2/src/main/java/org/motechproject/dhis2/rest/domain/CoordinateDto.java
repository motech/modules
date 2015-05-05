package org.motechproject.dhis2.rest.domain;

import java.util.Objects;

/**
 * A class to model coordinates for program stage events.
 */
public class CoordinateDto {
    private Double latitude;
    private Double longitude;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final CoordinateDto other = (CoordinateDto) obj;
        return Objects.equals(this.latitude, other.latitude)
                && Objects.equals(this.longitude, other.longitude);
    }
}