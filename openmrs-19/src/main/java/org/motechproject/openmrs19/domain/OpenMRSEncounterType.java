package org.motechproject.openmrs19.domain;


/**
 * Class representing a type of the encounter.
 */
public class OpenMRSEncounterType {

    private String uuid;
    private String name;
    private String description;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
