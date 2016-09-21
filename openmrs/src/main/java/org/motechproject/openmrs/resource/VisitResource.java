package org.motechproject.openmrs.resource;

import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.domain.Visit;
import org.motechproject.openmrs.domain.VisitType;

/**
 * Interface for visits management.
 */
public interface VisitResource {

    /**
     * Creates given visit on the OpenMRS server. The given {@code config} will be used while performing this action.
     *
     * @param config the name of the configuration
     * @param visit  the visit to be created
     * @return the saved visit
     */
    Visit createVisit(Config config, Visit visit);

    /**
     * Gets the visit by its UUID. The given {@code config} will be used while performing this action.
     *
     * @param config the name of the configuration
     * @param uuid   the UUID of the visit
     * @return the visit with the given UUID
     */
    Visit getVisitById(Config config, String uuid);

    /**
     * Deletes the visit with the given UUID from the OpenMRS server. The given {@code config} will be used while
     * performing this action.
     *
     * @param config the name of the configuration
     * @param uuid   the UUID of the visit
     */
    void deleteVisit(Config config, String uuid);

    /**
     * Creates given visit type on the OpenMRS server. The given {@code config} will be used while performing this
     * action.
     *
     * @param config    the name of the configuration
     * @param visitType the visit type to be created
     * @return the saved visit type
     */
    VisitType createVisitType(Config config, VisitType visitType);

    /**
     * Gets the visit type by its UUID. The given {@code config} will be used while performing this action.
     *
     * @param config the name of the configuration
     * @param uuid   the UUID of the visit type
     * @return the visit type with the given UUID
     */
    VisitType getVisitTypeById(Config config, String uuid);

    /**
     * Deletes the visit type with the given UUID from the OpenMRS server. The given {@code config} will be used
     * while performing this action.
     *
     * @param config the name of the configuration
     * @param uuid   the UUID of the visit type
     */
    void deleteVisitType(Config config, String uuid);
}
