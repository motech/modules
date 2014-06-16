package org.motechproject.appointments.domain;

/**
 * Enum class to set the status of an appointment
 */
public enum AppointmentStatus {
    // Default value for the appointment
    NONE,

    // Missed appointment
    MISSED,

    // Unscheduled visit to the clinic
    UNSCHEDULED,

    // Patient visited the clinic
    VISITED,

    // Appointment confirmed before the visit
    CONFIRMED,

    // Appointment was canceled by the patient or provider
    REMOVED,
}
