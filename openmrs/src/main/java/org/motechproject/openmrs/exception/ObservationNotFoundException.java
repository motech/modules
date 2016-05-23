package org.motechproject.openmrs.exception;

/**
 * Thrown when a OpenMRSObservation is null while performing operations on it.
 *
 * E.g. in voiding an OpenMRSObservation, if observation is not found this exception is thrown.
 */
public class ObservationNotFoundException extends Exception {
    public ObservationNotFoundException(String message) {
        super(message);
    }

    public ObservationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
