package org.motechproject.openmrs.service;

import org.motechproject.openmrs.domain.Visit;

/**
 * Interface for handling visits on the OpenMRS server.
 */
public interface OpenMRSVisitService {

    /**
     * Creates the given {@code visit} on the OpenMRS server. Configuration with the given {@code configName} will
     * be used while performing this action.
     *
     * @param configName the name of the configuration
     * @param visit      the visit to be created
     * @return the created visit
     */
    Visit createVisit(String configName, Visit visit);
}
