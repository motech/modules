package org.motechproject.openmrs19.resource.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single patient. It's a part of OpenMRS model.
 */
public class Patient {

    private String uuid;
    private List<Identifier> identifiers = new ArrayList<Identifier>();
    private Person person;

    /**
     * Returns the value of the identifier of the type with the given ID.
     *
     * @param uuid  the ID of the identifier type
     * @return the value of the identifier with the given ID
     */
    public Identifier getIdentifierByIdentifierType(String uuid) {
        for (Identifier identifier : identifiers) {
            IdentifierType type = identifier.getIdentifierType();
            if (type.getUuid().equals(uuid)) {
                return identifier;
            }
        }
        return null;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<Identifier> getIdentifiers() {
        return identifiers;
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
}
