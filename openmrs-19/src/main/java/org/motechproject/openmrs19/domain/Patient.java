package org.motechproject.openmrs19.domain;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.Expose;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a single patient.
 */
public class Patient {

    private String uuid;
    private String display;

    @Expose
    private List<Identifier> identifiers = new ArrayList<Identifier>();
    @Expose
    private Person person;

    private String motechId;
    private Location locationForMotechId;

    public Patient() {
    }

    public Patient(Person person, String motechId, Location locationForMotechId) {
        this(new ArrayList<>(), person, motechId, locationForMotechId);
    }

    public Patient(List<Identifier> identifiers, Person person, String motechId, Location locationForMotechId) {
        this.identifiers = identifiers;
        this.person = person;
        this.motechId = motechId;
        this.locationForMotechId = locationForMotechId;
    }

    /**
     * Implementation of the {@link JsonSerializer} interface for the {@link Patient}. It represents the patient as its
     * ID.
     */
    public static class PatientSerializer implements JsonSerializer<Patient> {
        @Override
        public JsonElement serialize(Patient src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getUuid());
        }
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

    public List<Identifier> getIdentifiers() {
        return identifiers;
    }

    // This getter is used in Task Data Provider to convert identifierList to map
    public Map<String, String> getIdentifiersMap() {
        Map<String, String> identifiersMap = new HashMap<>();

        for (Identifier identifier : this.identifiers) {
            identifiersMap.put(identifier.getIdentifierType().getName(), identifier.getIdentifier());
        }

        return identifiersMap;
    }

    public Identifier getIdentifierByTypeUuid(String uuid) {
        Identifier result = null;

        for (Identifier identifier : getIdentifiers()) {
            if (StringUtils.equals(uuid, identifier.getIdentifierType().getUuid())) {
                result = identifier;
                break;
            }
        }

        return result;
    }

    public void setIdentifiers(List<Identifier> identifiers) {
        this.identifiers = identifiers;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getMotechId() {
        return motechId;
    }

    public void setMotechId(String motechId) {
        this.motechId = motechId;
    }

    public Location getLocationForMotechId() {
        return locationForMotechId;
    }

    public void setLocationForMotechId(Location locationForMotechId) {
        this.locationForMotechId = locationForMotechId;
    }

    public void setMotechIdentifierValues(Identifier motechIdentifier) {
        this.motechId = motechIdentifier.getIdentifier();
        this.locationForMotechId = motechIdentifier.getLocation();
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, display, identifiers, person, motechId, locationForMotechId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Patient)) {
            return false;
        }

        Patient other = (Patient) o;

        return Objects.equals(this.uuid, other.uuid) && Objects.equals(this.display, other.display) &&
                Objects.equals(this.identifiers, other.identifiers) && Objects.equals(this.person, other.person) &&
                Objects.equals(this.motechId, other.motechId) && Objects.equals(this.locationForMotechId, other.locationForMotechId);
    }
}
