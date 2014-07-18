package org.motechproject.openmrs19.service;

import org.motechproject.openmrs19.domain.OpenMRSConcept;

import java.util.List;

/**
 * Interface for fetching and storing concept details.
 */
public interface OpenMRSConceptService {

    String resolveConceptUuidFromConceptName(String conceptName);

    /**
     * Saves a concept to the MRS system.
     *
     * @param  concept  Object to be saved
     * @return saved instance of the concept
     */
    OpenMRSConcept saveConcept(OpenMRSConcept concept);

    /**
     * Fetches a concept by the given concept ID.
     *
     * @param conceptId  value to be used to find a concept
     * @return concept with the given concept ID if exists
     */
    OpenMRSConcept getConcept(String conceptId);

    /**
     * Searches for concepts in the MRS system by concept's name.
     *
     * @param name  name of the concept to be searched for
     * @return list of matched Concepts
     */
    List<OpenMRSConcept> search(String name);

    List<OpenMRSConcept> getAllConcepts();

    void deleteConcept(String conceptId);
    OpenMRSConcept updateConcept(OpenMRSConcept concept);

}
