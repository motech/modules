package org.motechproject.openmrs19.resource.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
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

    public static class ProviderSerializer implements JsonSerializer<Provider> {
        @Override
        public JsonElement serialize(Provider src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getUuid());
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

}
