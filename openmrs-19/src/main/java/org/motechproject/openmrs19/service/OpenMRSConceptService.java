package org.motechproject.openmrs19.service;

import org.motechproject.openmrs19.domain.OpenMRSConcept;
import org.motechproject.openmrs19.exception.ConceptNameAlreadyInUseException;

import java.util.List;

/**
 * Interface for handling concepts on the OpenMRS server.
 */
public interface OpenMRSConceptService {

    /**
     * Returns the UUID of the concept with the given {@code name}.
     *
     * @param name  the name of the concept
     * @return the UUID of the concept
     */
    String resolveConceptUuidFromConceptName(String name);

    /**
     * Creates the given concept on the OpenMRS server.
     *
     * @param concept  the concept to be created
     * @return the created concept
     * @throws ConceptNameAlreadyInUseException if the concept with the given name already exists
     */
    OpenMRSConcept createConcept(OpenMRSConcept concept) throws ConceptNameAlreadyInUseException;

    /**
     * Returns the concept with the given {@code uuid}.
     *
     * @param uuid  the UUID of the concept
     * @return the concept with the given UUID
     */
    OpenMRSConcept getConceptByUuid(String uuid);

    /**
     * Returns a list of the concepts with the given {@code phrase} in their names.
     *
     * @param phrase  the phrase to search for
     * @return the list of concept with names containing the given phrase
     */
    List<OpenMRSConcept> search(String phrase);

    /**
     * Returns the basic information (UUID and name) about all the concepts stored on the OpenMRS server.
     *
     * @return the list of all concepts
     */
    List<OpenMRSConcept> getAllConcepts();

    /**
     * Deletes the concept with the given {@code uuid} from the OpenMRS server.
     *
     * @param uuid  the UUID of the concept
     */
    void deleteConcept(String uuid);

    /**
     * Updates the concept with the information stored in the given concept. Fields name, names and display name will be
     * ignored as changing them is not allowed by the OpenMRS system.
     *
     * @param concept  the concept used as an update source
     * @return the updated concept
     */
    OpenMRSConcept updateConcept(OpenMRSConcept concept);

    /**
     * Returns a list of the concepts. The returned list will contain maximum of {@code pageSize} concepts fetched from
     * the page with the given {@code page} number. The list might contain less entries if the given {@code page} is the
     * last one and there is less than {@code pageSize} concepts on it.
     *
     * @param page  the number of the page
     * @param pageSize  the size of the page
     * @return  the list of concepts on the given page
     */
    List<OpenMRSConcept> getConcepts(int page, int pageSize);

}
