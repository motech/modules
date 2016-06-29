package org.motechproject.rapidpro.exception;

/**
 * Thrown when there is an error serializing or deserializing to or from JSON.
 */
public class JsonUtilException extends Exception {

    public JsonUtilException(String message) {
        super(message);
    }

    public JsonUtilException(String message, Throwable cause) {
        super(message, cause);
    }
}
