package org.motechproject.openmrs19.service;

import org.motechproject.openmrs19.domain.OpenMRSConcept;
import org.motechproject.openmrs19.exception.ConceptNameAlreadyInUseException;

import java.util.List;

/**
 * Interface for fetching and storing concept details.
 */
public interface OpenMRSConceptService {

    String resolveConceptUuidFromConceptName(String conceptName);

    /**
     * Creates a concept on the OpenMRS server
     *
     * @param  concept  the concept to be created
     * @return saved instance of the concept
     * @throws ConceptNameAlreadyInUseException  when name of the given in concept is already used by another one
     */
    OpenMRSConcept createConcept(OpenMRSConcept concept) throws ConceptNameAlreadyInUseException;

    /**
     * Fetches a concept by the given concept ID.
     *
     * @param uuid  value to be used to find a concept
     * @return concept with the given concept ID if exists
     */
    OpenMRSConcept getConceptById(String uuid);

    /**
     * Searches for concepts in the MRS system by concept's name.
     *
     * @param name  name of the concept to be searched for
     * @return list of matched Concepts
     */
    List<OpenMRSConcept> search(String name);

    /**
     * Returns basic information (UUID and name) about all the concepts stored on the server.
     *
     * @return  the list of all concepts
     */
    List<OpenMRSConcept> getAllConcepts();

    /**
     * Deletes concept with given UUID from the OpenMRS server.
     *
     * @param uuid  the UUID of the concept
     */
    void deleteConcept(String uuid);

    /**
     * Updates concept with information stored in given concept. Fields name, names and display will be ignored as
     * changing them is not allowed by the OpenMRS.
     *
     * @param concept  the concept used as an update source
     * @return  the concept with updated fields
     */
    OpenMRSConcept updateConcept(OpenMRSConcept concept);

    /**
     * Fetches page with given number with size defined in {@code pageSize}.
     *
     * @param page  the number of the page
     * @param pageSize  the size of the page
     * @return  the list of concept on the given page
     */
    List<OpenMRSConcept> getConcepts(int page, int pageSize);

}
