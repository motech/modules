package org.motechproject.appointments.domain;

/**
 * Appointment exception class to wrap errors in the Module with custom messages
 */
public class AppointmentException extends Exception {

    public AppointmentException(String message) {
        super(message);
    }

    public AppointmentException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
