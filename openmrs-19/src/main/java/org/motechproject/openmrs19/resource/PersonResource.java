package org.motechproject.openmrs19.resource;

import org.motechproject.openmrs19.domain.Attribute;
import org.motechproject.openmrs19.domain.AttributeTypeListResult;
import org.motechproject.openmrs19.domain.Person;
import org.motechproject.openmrs19.config.Config;

/**
 * Interface for persons management.
 */
public interface PersonResource {

    /**
     * Gets person by its UUID. The given {@code config} will be used while performing this action.
     *
     * @param config  the configuration to be used while performing this action
     * @param uuid  the UUID of the person
     * @return  the person with the given UUID
     */
    Person getPersonById(Config config, String uuid);

    /**
     * Creates the given person on the OpenMRS server. The given {@code config} will be used while performing this
     * action.
     *
     * @param config  the configuration to be used while performing this action
     * @param person  the person to be creating
     * @return  the saved person
     */
    Person createPerson(Config config, Person person);

    /**
     * Creates attribute for the person with given UUID. The given {@code config} will be used while performing this
     * action.
     *
     * @param config  the configuration to be used while performing this action
     * @param uuid  the UUID of the person
     * @param attribute  the attribute to be saved
     */
    void createPersonAttribute(Config config, String uuid, Attribute attribute);

    /**
     * Returns {@code AttributeTypeListResult} of all the attributes matching given name. The given {@code config} will
     * be used while performing this action.
     *
     * @param config  the configuration to be used while performing this action
     * @param name  the name to be matched
     * @return  the list of matching attributes
     */
    AttributeTypeListResult queryPersonAttributeTypeByName(Config config, String name);

    /**
     * Deletes the given attribute for person with given UUID. The given {@code config} will be used while performing
     * this action.
     *
     * @param config  the configuration to be used while performing this action
     * @param uuid  the UUID of the person
     * @param attribute  the attribute to be deleted
     */
    void deleteAttribute(Config config, String uuid, Attribute attribute);

    /**
     * Updates the person with the given data. The given {@code config} will be used while performing this action.
     *
     * @param config  the configuration to be used while performing this action
     * @param person  the update source
     * @return  the updated person
     */
    Person updatePerson(Config config, Person person);

    /**
     * Updates the preferred name of the person with given UUID. The given {@code config} will be used while performing
     * this action.
     *
     * @param config  the configuration to be used while performing this action
     * @param uuid  the UUID of the person
     * @param name  the new preferred name
     */
    void updatePersonName(Config config, String uuid, Person.Name name);

    /**
     * Update the preferred address of the person with given UUID. The given {@code config} will be used while
     * performing this action.
     *
     * @param config  the configuration to be used while performing this action
     * @param uuid  the UUID of the person
     * @param address  the new preferred address
     */
    void updatePersonAddress(Config config, String uuid, Person.Address address);

    /**
     * Deletes the person with the given UUID from the OpenMRs server. The given {@code config} will be used while
     * performing this action.
     *
     * @param config  the configuration to be used while performing this action
     * @param personUuid  the UUID of the person
     */
    void deletePerson(Config config, String personUuid);
}
