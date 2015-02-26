package org.motechproject.csd.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

@Entity
public class Geocode {

    @Field(required = true)
    private double latitude;

    @Field(required = true)
    private double longitude;

    @Field
    private double altitude;

    @Field
    private String coordinateSystem;

    public Geocode() {
    }

    public Geocode(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Geocode(double latitude, double longitude, double altitude, String coordinateSystem) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.coordinateSystem = coordinateSystem;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
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

        if (Double.compare(geocode.altitude, altitude) != 0) {
            return false;
        }
        if (Double.compare(geocode.latitude, latitude) != 0) {
            return false;
        }
        if (Double.compare(geocode.longitude, longitude) != 0) {
            return false;
        }
        if (coordinateSystem != null ? !coordinateSystem.equals(geocode.coordinateSystem) : geocode.coordinateSystem != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(latitude);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(altitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
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
