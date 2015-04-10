package org.motechproject.csd.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.UIDisplayable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for geocode complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="geocode">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="latitude" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="longitude" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="altitude" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="coordinateSystem" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@Entity
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(propOrder = { "latitude", "longitude", "altitude", "coordinateSystem" })
public class Geocode {

    @UIDisplayable(position = 0)
    @Field(required = true)
    private double latitude;

    @UIDisplayable(position = 1)
    @Field(required = true)
    private double longitude;

    @UIDisplayable(position = 2)
    @Field
    private double altitude;

    @UIDisplayable(position = 3)
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

    @XmlElement(required = true)
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @XmlElement(required = true)
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    @XmlElement
    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public String getCoordinateSystem() {
        return coordinateSystem;
    }

    @XmlElement
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

        if (Double.compare(geocode.latitude, latitude) != 0) {
            return false;
        }
        if (Double.compare(geocode.longitude, longitude) != 0) {
            return false;
        }
        if (Double.compare(geocode.altitude, altitude) != 0) {
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
        return "latitude=" + latitude +
                " longitude=" + longitude;
    }
}
