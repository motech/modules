package org.motechproject.openmrs19.resource.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Provides a representation of the OpenMRS provider, as well as JSON serializer
 * for this class.
 */
public class Provider {
    private String uuid;
    private Person person;
    private String identifier;

    public static class ProviderSerializer implements JsonSerializer<Provider> {
        @Override
        public JsonElement serialize(Provider src, Type typeOfSrc, JsonSerializationContext context) {

            JsonObject object = new JsonObject();

            if (src.uuid != null) {
                object.addProperty("uuid", src.uuid);
            }
            if (src.person != null) {
                object.addProperty("person", src.person.getUuid());
            }
            if (src.identifier != null) {
                object.addProperty("identifier", src.identifier);
            }

            return object;
        }
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

}
