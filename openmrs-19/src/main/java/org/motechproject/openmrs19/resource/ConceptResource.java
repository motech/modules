package org.motechproject.openmrs19.resource;

import org.motechproject.openmrs19.domain.Concept;
import org.motechproject.openmrs19.domain.ConceptListResult;
import org.motechproject.openmrs19.config.Config;

/**
 * Interface for concepts management.
 */
public interface ConceptResource {

    /**
     * Fetches all concepts with {@code name} in their names. The given {@code config} will be used while performing
     * this action.
     *
     * @param config  the configuration to be used while performing this action
     * @param name  the string to be included in the concept name
     * @return  the list of concepts containing {@code name} in their name
     */
    ConceptListResult queryForConceptsByName(Config config, String name);

    /**
     * Gets concept by its name. The given {@code config} will be used while performing this action.
     *
     * @param config  the configuration to be used while performing this action
     * @param name  the name of the concept
     * @return  the concept with given name
     */
    Concept getConceptByName(Config config, String name);

    /**
     * Gets concept by its UUID. The given {@code config} will be used while performing this action.
     *
     * @param config  the configuration to be used while performing this action
     * @param uuid  the uuid of the concept
     * @return  the concept with given UUID
     */
    Concept getConceptById(Config config, String uuid);

    /**
     * Creates the given concept on the OpenMRS server. The given {@code config} will be used while performing this
     * action.
     *
     * @param config  the configuration to be used while performing this action
     * @param concept  the concept to be created
     * @return  the created concept
     */
    Concept createConcept(Config config, Concept concept);

    /**
     * Fetches basic information (UUID and name) of all concepts stored on the server. The given {@code config} will be
     * used while performing this action.
     *
     * @param config  the configuration to be used while performing this action
     * @return  the list of all concepts
     */
    ConceptListResult getAllConcepts(Config config);

    /**
     * Updates concept with the given data. The given {@code config} will be used while performing this action.
     *
     * @param config  the configuration to be used while performing this action
     * @param concept  the concept used as update source
     * @return  the updated concept
     */
    Concept updateConcept(Config config, Concept concept);

    /**
     * Deletes the concept with the given UUID from the OpenMRS server. The given {@code config} will be used while
     * performing this action.
     *
     * @param config  the configuration to be used while performing this action
     * @param uuid  the UUID of the concept
     */
    void deleteConcept(Config config, String uuid);

    /**
     * Fetches page with given number with size defined in {@code pageSize}. Page numeration starts with 1. The given
     * {@code config} will be used while performing this action.
     *
     * @param config  the configuration to be used while performing this action
     * @param page  the number of the page
     * @param pageSize  the size of the page
     * @return  the list of concepts on the given page
     */
    ConceptListResult getConcepts(Config config, int page, int pageSize);
}
