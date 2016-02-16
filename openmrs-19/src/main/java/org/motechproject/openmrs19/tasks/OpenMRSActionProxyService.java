package org.motechproject.openmrs19.tasks;

import org.joda.time.DateTime;

/**
 * Proxy registered with the task channel, exposing the OpenMRS service as task actions.
 */
public interface OpenMRSActionProxyService {

    /**
     * Creates an encounter with the given {@code encounterDate}, {@code encounterType}, {@code locationName},
     * {@code patientUuid} and {@code providerUuid}. The locationName is the only not required field.
     *
     * @param encounterDate the date of encounter
     * @param encounterType the type of encounter
     * @param locationName the name of location
     * @param patientUuid the patient uuid
     * @param providerUuid the provider uuid
     */
    void createEncounter(DateTime encounterDate, String encounterType, String locationName, String patientUuid, String providerUuid);
}
