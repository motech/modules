package org.motechproject.openmrs19.resource.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

/**
 * Represents a single OpenMRS encounter. An encounter is a single, specific interaction between the patient and a
 * provider. It can be any interaction and includes doctor visits, laboratory tests, food distribution, home visits,
 * counselor appointments, etc. It's a part of the OpenMRS model.
 */
public class Encounter {

    private String uuid;
    private Location location;
    private EncounterType encounterType;
    private Date encounterDatetime;
    private Patient patient;
    private Person provider;
    private List<Observation> obs;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public EncounterType getEncounterType() {
        return encounterType;
    }

    public void setEncounterType(EncounterType encounterType) {
        this.encounterType = encounterType;
    }

    public Date getEncounterDatetime() {
        return encounterDatetime;
    }

    public void setEncounterDatetime(Date encounterDatetime) {
        this.encounterDatetime = encounterDatetime;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Person getProvider() {
        return provider;
    }

    public void setProvider(Person provider) {
        this.provider = provider;
    }

    public List<Observation> getObs() {
        return obs;
    }

    public void setObs(List<Observation> obs) {
        this.obs = obs;
    }

    /**
     * Represent a single type of the encounter.
     */
    public static class EncounterType {

        private String uuid;
        private String name;
        private String description;

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

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }
    }

    /**
     * Implementation of the {@link JsonSerializer} interface for the
     * {@link org.motechproject.openmrs19.resource.model.Encounter.EncounterType} class. It represents the encounter
     * as its name.
     */
    public static class EncounterTypeSerializer implements JsonSerializer<EncounterType> {

        @Override
        public JsonElement serialize(EncounterType src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getName());
        }

    }
}
