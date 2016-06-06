package org.motechproject.openmrs.domain;

import com.google.gson.annotations.Expose;

import java.util.Objects;

/**
 * Represents a single patient identifier. A patient identifier is any unique number that can identify a patient.
 * Examples are a Medical Record Number, a National ID, a Social Security Number, a driver's license number, etc.
 * A patient can have any number of identifiers.
 */
public class Identifier {

    private String uuid;
    private String display;

    @Expose
    private String identifier;
    @Expose
    private IdentifierType identifierType;
    @Expose
    private Location location;

    public Identifier() {
    }

    public Identifier(String identifier, IdentifierType identifierType) {
        this(identifier, identifierType, null);
    }

    public Identifier(String identifier, IdentifierType identifierType, Location location) {
        this.identifier = identifier;
        this.identifierType = identifierType;
        this.location = location;
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

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public IdentifierType getIdentifierType() {
        return identifierType;
    }

    public void setIdentifierType(IdentifierType identifierType) {
        this.identifierType = identifierType;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, display, identifier, identifierType, location);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Identifier)) {
            return false;
        }

        Identifier other = (Identifier) o;

        return Objects.equals(this.uuid, other.uuid) && Objects.equals(this.display, other.display) &&
                Objects.equals(this.identifier, other.identifier) && Objects.equals(this.identifierType, other.identifierType) &&
                Objects.equals(this.location, other.location);
    }
}
