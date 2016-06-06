package org.motechproject.openmrs.service;

import org.motechproject.openmrs.domain.Concept;
import org.motechproject.openmrs.exception.ConceptNameAlreadyInUseException;

import java.util.List;

/**
 * Interface for handling concepts on the OpenMRS server.
 */
public interface OpenMRSConceptService {

    /**
     * Returns the UUID of the concept with the given {@code name}. Configuration with the given {@code configName} will
     * be used while performing this action.
     *
     * @param configName  the name of the configuration
     * @param name  the name of the concept
     * @return the UUID of the concept
     */
    String resolveConceptUuidFromConceptName(String configName, String name);

    /**
     * Creates the given concept on the OpenMRS server. Configuration with the given {@code configName} will be used
     * while performing this action.
     *
     * @param configName  the name of the configuration
     * @param concept  the concept to be created
     * @return the created concept
     * @throws ConceptNameAlreadyInUseException if the concept with the given name already exists
     */
    Concept createConcept(String configName, Concept concept) throws ConceptNameAlreadyInUseException;


    /**
     * Returns the concept with the given {@code uuid}. Configuration with the given {@code configName} will be used
     * while performing this action.
     *
     * @param configName  the name of the configuration
     * @param uuid  the UUID of the concept
     * @return the concept with the given UUID
     */
    Concept getConceptByUuid(String configName, String uuid);

    /**
     * Returns a list of the concepts with the given {@code phrase} in their names. Configuration with the given
     * {@code configName} will be used while performing this action.
     *
     * @param configName  the name of the configuration
     * @param phrase  the phrase to search for
     * @return the list of concept with names containing the given phrase
     */
    List<Concept> search(String configName, String phrase);

    /**
     * Returns the basic information (UUID and name) about all the concepts stored on the OpenMRS server. Configuration
     * with the given {@code configName} will be used while performing this action.
     *
     * @param configName  the name of the configuration
     * @return the list of all concepts
     */
    List<Concept> getAllConcepts(String configName);

    /**
     * Deletes the concept with the given {@code uuid} from the OpenMRS server. Configuration with the given
     * {@code configName} will be used while performing this action.
     *
     * @param configName  the name of the configuration
     * @param uuid  the UUID of the concept
     */
    void deleteConcept(String configName, String uuid);

    /**
     * Updates the concept with the information stored in the given concept. Fields name, names and display name will be
     * ignored as changing them is not allowed by the  system. Configuration with the given {@code configName}
     * will be used while performing this action.
     *
     * @param configName  the name of the configuration
     * @param concept  the concept used as an update source
     * @return the updated concept
     */
    Concept updateConcept(String configName, Concept concept);

    /**
     * Returns a list of the concepts. The returned list will contain maximum of {@code pageSize} concepts fetched from
     * the page with the given {@code page} number. The list might contain less entries if the given {@code page} is the
     * last one and there is less than {@code pageSize} concepts on it. Configuration with the given {@code configName}
     * will be used while performing this action.
     *
     * @param configName  the name of the configuration
     * @param page  the number of the page
     * @param pageSize  the size of the page
     * @return  the list of concepts on the given page
     */
    List<Concept> getConcepts(String configName, int page, int pageSize);

}
