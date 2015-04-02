package org.motechproject.openmrs19.resource;

import org.motechproject.openmrs19.exception.HttpException;
import org.motechproject.openmrs19.resource.model.Concept;
import org.motechproject.openmrs19.resource.model.ConceptListResult;

/**
 * Interface for concepts management.
 */
public interface ConceptResource {

    /**
     * Fetches all concepts with {@code name} in their names.
     *
     * @param name  the string to be included in the concept name
     * @return  the list of concepts containing {@code name} in their name
     * @throws HttpException  when there was problem while fetching information
     */
    ConceptListResult queryForConceptsByName(String name) throws HttpException;

    /**
     * Gets concept by its name.
     *
     * @param name  the name of the concept
     * @return  the concept with given name
     * @throws HttpException  when there was problem while fetching information
     */
    Concept getConceptByName(String name) throws HttpException;

    /**
     * Gets concept by its UUID.
     *
     * @param uuid  the uuid of the concept
     * @return  the concept with given UUID
     * @throws HttpException  when there was problem while fetching information
     */
    Concept getConceptById(String uuid) throws HttpException;

    /**
     * Creates the given concept on the OpenMRS server.
     *
     * @param concept  the concept to be created
     * @return  the created concept
     * @throws HttpException  when there was problem while creating concept
     */
    Concept createConcept(Concept concept) throws HttpException;

    /**
     * Fetches basic information (UUID and name) of all concepts stored on the server.
     *
     * @return  the list of all concepts
     * @throws HttpException  when there was problem while fetching information
     */
    ConceptListResult getAllConcepts() throws HttpException;

    /**
     * Updates concept with the given data.
     *
     * @param concept  the concept used as update source
     * @return  the updated concept
     * @throws HttpException  when there was problem while updating concept
     */
    Concept updateConcept(Concept concept) throws HttpException;

    /**
     * Deletes the concept with the given UUID from the OpenMRS server.
     *
     * @param uuid  the UUID of the concept
     * @throws HttpException  when there was problem while deleting concept
     */
    void deleteConcept(String uuid) throws HttpException;

    /**
     * Fetches page with given number with size defined in {@code pageSize}. Page numeration starts with 1.
     *
     * @param page  the number of the page
     * @param pageSize  the size of the page
     * @return  the list of concepts on the given page
     * @throws HttpException  when there was problem while fetching information
     */
    ConceptListResult getConcepts(int page, int pageSize) throws HttpException;
}
