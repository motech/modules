package org.motechproject.csd.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

@Entity
public class Geocode {

    @Field(required = true)
    private long latitude;

    @Field(required = true)
    private long longitude;

    @Field
    private long altitude;

    @Field
    private String coordinateSystem;

    public Geocode() {
    }

    public Geocode(long latitude, long longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Geocode(long latitude, long longitude, long altitude, String coordinateSystem) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.coordinateSystem = coordinateSystem;
    }

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public long getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    public long getAltitude() {
        return altitude;
    }

    public void setAltitude(long altitude) {
        this.altitude = altitude;
    }

    public String getCoordinateSystem() {
        return coordinateSystem;
    }

    public void setCoordinateSystem(String coordinateSystem) {
        this.coordinateSystem = coordinateSystem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Geocode geocode = (Geocode) o;

        if (altitude != geocode.altitude) {
            return false;
        }
        if (latitude != geocode.latitude) {
            return false;
        }
        if (longitude != geocode.longitude) {
            return false;
        }
        if (coordinateSystem != null ? !coordinateSystem.equals(geocode.coordinateSystem) : geocode.coordinateSystem != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (latitude ^ (latitude >>> 32));
        result = 31 * result + (int) (longitude ^ (longitude >>> 32));
        result = 31 * result + (int) (altitude ^ (altitude >>> 32));
        result = 31 * result + (coordinateSystem != null ? coordinateSystem.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Geocode{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", altitude=" + altitude +
                ", coordinateSystem='" + coordinateSystem + '\'' +
                '}';
    }
}
