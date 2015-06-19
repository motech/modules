package org.motechproject.openmrs19.resource.model;

/**
 * Represents a single patient identifier. A patient identifier is any unique number that can identify a patient.
 * Examples are a Medical Record Number, a National ID, a Social Security Number, a driver's license number, etc.
 * A patient can have any number of identifiers. It's a part of the OpenMRS model.
 */
public class Identifier {

    private String uuid;
    private String identifier;
    private IdentifierType identifierType;
    private Location location;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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
}
