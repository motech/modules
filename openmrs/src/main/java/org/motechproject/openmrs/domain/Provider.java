package org.motechproject.openmrs.domain;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * Represents a single provider. Provider is a clinician responsible for providing care to a patient.
 */
public class Provider {

    private String uuid;
    private Person person;
    private String identifier;

    /**
     * Default constructor.
     */
    public Provider() {
        this(null);
    }

    /**
     * Creates a provider based on the given {@code person} details.
     *
     * @param person  the personal information about the provider
     */
    public Provider(Person person) {
        this.person = person;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * Implementation of the {@link JsonSerializer} interface for the {@link Provider} class. It represents the provider
     * as its ID.
     */
    public static class ProviderSerializer implements JsonSerializer<Provider> {

        @Override
        public JsonElement serialize(Provider provider, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(provider.getUuid());
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, person, identifier);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Provider other = (Provider) obj;

        return Objects.equals(this.uuid, other.uuid) &&
                Objects.equals(this.person, other.person) &&
                Objects.equals(this.identifier, other.identifier);
    }
}
