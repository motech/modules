package org.motechproject.rapidpro.util;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.type.TypeReference;
import org.motechproject.rapidpro.exception.JsonUtilException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Utility class for serializing and deserializing objects to or from JSON
 */
public final class JsonUtils {

    private static final String ERROR_SERIALIZING = "Error serializing object of type: ";
    private static final String ERROR_DESERIALIZING = "Error deserializing object of type: ";

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(SerializationConfig.Feature.WRITE_NULL_MAP_VALUES, false);
    }

    private JsonUtils() {
    }

    /**
     * Deserializes a JSON inputstream to an object
     *
     * @param inputStream   The serialized object
     * @param typeReference The type of the object
     * @return An object of the type specified in the type reference
     * @throws JsonUtilException if there is an error deserializing
     */
    public static Object toObject(InputStream inputStream, TypeReference typeReference) throws JsonUtilException {
        try {
            return mapper.readValue(inputStream, typeReference);
        } catch (IOException e) {
            throw new JsonUtilException(ERROR_DESERIALIZING + typeReference.getType(), e);
        }
    }

    /**
     * Serializes an Object to a JSON byte array
     *
     * @param o The object to be serialized
     * @return A byte array serial representation of the object
     * @throws JsonUtilException If there is an error serializing
     */
    public static byte[] toByteArray(Object o) throws JsonUtilException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            buffer = new ByteArrayOutputStream();
            mapper.writeValue(buffer, o);
            return buffer.toByteArray();

        } catch (IOException e) {
            throw new JsonUtilException(ERROR_SERIALIZING + o.getClass().getSimpleName(), e);

        } finally {
            closeOutputStream(buffer);
        }
    }

    private static void closeOutputStream(OutputStream os) {
        try {
            os.close();
        } catch (IOException e) { }
    }

    /**
     * Serializes an object to a JSON String
     *
     * @param o The object to be serialized
     * @return The JSON String representaton of the object
     * @throws JsonUtilException If there is an error serializing
     */
    public static String toString(Object o) throws JsonUtilException {
        try {
            return mapper.writeValueAsString(o);
        } catch (IOException e) {
            throw new JsonUtilException(ERROR_DESERIALIZING + o.getClass().getSimpleName(), e);
        }
    }
}
