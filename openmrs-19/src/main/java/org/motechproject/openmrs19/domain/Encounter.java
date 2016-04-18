package org.motechproject.openmrs19.domain;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single OpenMRS encounter. An encounter is a single, specific interaction between the patient and a
 * provider. It can be any interaction and includes doctor visits, laboratory tests, food distribution, home visits,
 * counselor appointments, etc.
 */
public class Encounter {

    private String uuid;
    private String display;
    private Location location;
    private EncounterType encounterType;
    private Date encounterDatetime;
    private Patient patient;
    private Person provider;
    private List<Observation> obs;

    /**
     * Default constructor.
     */
    public Encounter() {
    }

    public Encounter(Location location, EncounterType encounterType, Date encounterDatetime, Patient patient,
                     Person provider) {
        this(location, encounterType, encounterDatetime, patient, provider, null);
    }

    public Encounter(Location location, EncounterType encounterType, Date encounterDatetime, Patient patient,
                     Person provider, List<Observation> obs) {
        this.location = location;
        this.encounterType = encounterType;
        this.encounterDatetime = encounterDatetime;
        this.patient = patient;
        this.provider = provider;
        this.obs = obs;
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

    @Override
    public int hashCode() {
        return Objects.hash(uuid, display, location, encounterType, encounterDatetime, patient, provider, obs);
    }

    @Override //NO CHECKSTYLE Cyclomatic Complexity
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Encounter other = (Encounter) obj;

        return Objects.equals(this.uuid, other.uuid) && Objects.equals(this.display, other.display) &&
                Objects.equals(this.location, other.location) && Objects.equals(this.encounterType, other.encounterType) &&
                Objects.equals(this.encounterDatetime, other.encounterDatetime) && Objects.equals(this.patient, other.patient) &&
                Objects.equals(this.provider, other.provider) && Objects.equals(this.obs, other.obs);
    }
}
