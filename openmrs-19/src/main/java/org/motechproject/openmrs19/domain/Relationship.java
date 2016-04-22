package org.motechproject.openmrs19.domain;

import java.util.Objects;

/**
 * Represents a single relationship between two persons from the OpenMRS server.
 */
public class Relationship {

    private String uuid;

    private RelationshipType relationshipType;

    private Person personA;

    private Person personB;

    private String startDate;

    private String endDate;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public RelationshipType getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(RelationshipType relationshipType) {
        this.relationshipType = relationshipType;
    }

    public Person getPersonA() {
        return personA;
    }

    public void setPersonA(Person personA) {
        this.personA = personA;
    }

    public Person getPersonB() {
        return personB;
    }

    public void setPersonB(Person personB) {
        this.personB = personB;
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

    @Override
    public int hashCode() {
        return Objects.hash(uuid, relationshipType, personA, personB, startDate, endDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Relationship)) {
            return false;
        }

        Relationship other = (Relationship) o;

        return Objects.equals(uuid, other.uuid) && Objects.equals(relationshipType, other.relationshipType)
                && Objects.equals(personA, other.personA) && Objects.equals(personB, other.personB)
                && Objects.equals(startDate, other.startDate) && Objects.equals(endDate, other.endDate);
    }
}
