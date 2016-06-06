package org.motechproject.openmrs.domain;

import java.util.Objects;

/**
 * Represents a single type of the relationship from the OpenMRS server.
 */
public class RelationshipType {

    private String uuid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof RelationshipType)) {
            return false;
        }

        RelationshipType other = (RelationshipType) o;

        return Objects.equals(uuid, other.uuid);
    }
}
