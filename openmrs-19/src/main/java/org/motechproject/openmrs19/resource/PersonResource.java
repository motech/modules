package org.motechproject.openmrs19.resource;

import org.motechproject.openmrs19.exception.HttpException;
import org.motechproject.openmrs19.resource.model.Attribute;
import org.motechproject.openmrs19.resource.model.AttributeTypeListResult;
import org.motechproject.openmrs19.resource.model.Person;
import org.motechproject.openmrs19.resource.model.Person.PreferredAddress;
import org.motechproject.openmrs19.resource.model.Person.PreferredName;

/**
 * Interface for persons management.
 */
public interface PersonResource {

    /**
     * Gets person by its UUID.
     *
     * @param uuid  the UUID of the person
     * @return  the person with the given UUID
     * @throws HttpException  when there were problems while fetching person
     */
    Person getPersonById(String uuid) throws HttpException;

    /**
     * Creates the given person on the OpenMRS server.
     *
     * @param person  the person to be creating
     * @return  the saved person
     * @throws HttpException  when there were problems while creating person
     */
    Person createPerson(Person person) throws HttpException;

    /**
     * Creates attribute for the person with given UUID.
     *
     * @param uuid  the UUID of the person
     * @param attribute  the attribute to be saved
     * @throws HttpException  when there were problems while creating attribute
     */
    void createPersonAttribute(String uuid, Attribute attribute) throws HttpException;

    /**
     * Returns {@code AttributeTypeListResult} of all the attributes matching given name.
     *
     * @param name  the name to be matched
     * @return  the list of matching attributes
     * @throws HttpException  when there were problems while fetching attributes
     */
    AttributeTypeListResult queryPersonAttributeTypeByName(String name) throws HttpException;

    /**
     * Deletes the given attribute for person with given UUID.
     *
     * @param uuid  the UUID of the person
     * @param attribute  the attribute to be deleted
     * @throws HttpException  when there were problems while deleting attribute
     */
    void deleteAttribute(String uuid, Attribute attribute) throws HttpException;

    /**
     * Updates the person with the given data.
     *
     * @param person  the update source
     * @return  the updated person
     * @throws HttpException  when there were problems while updating person
     */
    Person updatePerson(Person person) throws HttpException;

    /**
     * Updates the preferred name of the person with given UUID.
     *
     * @param uuid  the UUID of the person
     * @param name  the new preferred name
     * @throws HttpException  when there were problems while updating person name
     */
    void updatePersonName(String uuid, PreferredName name) throws HttpException;

    /**
     * Update the preferred address of the person with given UUID.
     *
     * @param uuid  the UUID of the person
     * @param address  the new preferred address
     * @throws HttpException
     */
    void updatePersonAddress(String uuid, PreferredAddress address) throws HttpException;

    /**
     * Deletes the person with the given UUID from the OpenMRs server.
     *
     * @param personUuid  the UUID of the person
     * @throws HttpException  when there were problems while deleting person
     */
    void deletePerson(String personUuid) throws HttpException;

}
