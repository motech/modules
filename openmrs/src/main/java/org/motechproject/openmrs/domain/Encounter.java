package org.motechproject.openmrs.domain;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.motechproject.openmrs.util.JsonUtils;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Collections;
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
    private Visit visit;
    private List<Observation> obs;
    private List<Person> encounterProviders;

    /**
     * Default constructor.
     */
    public Encounter() {
    }

    public Encounter(Location location, EncounterType encounterType, Date encounterDatetime, Patient patient,
                     List<Person> encounterProviders) {
        this(location, encounterType, encounterDatetime, patient, null, encounterProviders, null);
    }

    public Encounter(Location location, EncounterType encounterType, Date encounterDatetime, Patient patient,
                     Visit visit, List<Person> encounterProviders) {
        this(location, encounterType, encounterDatetime, patient, visit, encounterProviders, null);
    }

    public Encounter(Location location, EncounterType encounterType, Date encounterDatetime, Patient patient,
                     Visit visit, List<Person> encounterProviders, List<Observation> obs) {
        this.location = location;
        this.encounterType = encounterType;
        this.encounterDatetime = encounterDatetime;
        this.patient = patient;
        this.visit = visit;
        this.encounterProviders = encounterProviders;
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

    public Visit getVisit() {
        return visit;
    }

    public void setVisit(Visit visit) {
        this.visit = visit;
    }

    public List<Person> getEncounterProviders() {
        return encounterProviders;
    }

    public void setEncounterProviders(List<Person> encounterProviders) {
        this.encounterProviders = encounterProviders;
    }

    public List<Observation> getObs() {
        return obs;
    }

    public void setObs(List<Observation> obs) {
        this.obs = obs;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, display, location, encounterType, encounterDatetime, patient, visit, obs, encounterProviders);
    }

    @Override //NO CHECKSTYLE Cyclomatic Complexity
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Encounter encounter = (Encounter) o;
        return Objects.equals(uuid, encounter.uuid) &&
                Objects.equals(display, encounter.display) &&
                Objects.equals(location, encounter.location) &&
                Objects.equals(encounterType, encounter.encounterType) &&
                Objects.equals(encounterDatetime, encounter.encounterDatetime) &&
                Objects.equals(patient, encounter.patient) &&
                Objects.equals(visit, encounter.visit) &&
                Objects.equals(obs, encounter.obs) &&
                Objects.equals(encounterProviders, encounter.encounterProviders);
    }

    /**
     * Implementation of the {@link JsonSerializer} interface for the {@link Encounter} class.
     */
    public static class EncounterSerializer implements JsonSerializer<Encounter> {

        @Override
        public JsonElement serialize(Encounter src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject encounter = new JsonObject();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            if (src.uuid != null) {
                encounter.addProperty("uuid", src.getUuid());
            }
            if (src.display != null) {
                encounter.addProperty("display", src.getDisplay());
            }
            if (src.location != null) {
                encounter.addProperty("location", src.getLocation().getUuid());
            }
            if (src.encounterType != null) {
                encounter.addProperty("encounterType", src.getEncounterType().getName());
            }
            if (src.encounterDatetime != null) {
                encounter.addProperty("encounterDatetime", sdf.format(src.getEncounterDatetime()));
            }
            if (src.patient != null) {
                encounter.addProperty("patient", src.getPatient().getUuid());
            }
            if (src.encounterProviders != null) {
                encounter.addProperty("provider", src.getEncounterProviders().get(0).getUuid());
            }
            if (src.visit != null) {
                encounter.addProperty("visit", src.getVisit().getUuid());
            }
            if (src.obs != null) {
                final JsonElement jsonObs = context.serialize(src.getObs());
                encounter.add("obs", jsonObs);
            }
            return encounter;
        }
    }

    /**
     * Implementation of the {@link JsonDeserializer} interface for the {@link Encounter} class.
     */
    public static class EncounterDeserializer implements JsonDeserializer<Encounter> {
        @Override
        public Encounter deserialize(final JsonElement src, final Type typeOfT, final JsonDeserializationContext context) {

            final JsonObject jsonEncounter = src.getAsJsonObject();

            final JsonElement jsonProvider = jsonEncounter.get("provider");
            Person provider = (Person) JsonUtils.readJson(jsonProvider.toString(), Person.class);

            Encounter encounter = (Encounter) JsonUtils.readJson(src.toString(), Encounter.class);
            encounter.setEncounterProviders(Collections.singletonList(provider));

            return encounter;
        }
    }
}
