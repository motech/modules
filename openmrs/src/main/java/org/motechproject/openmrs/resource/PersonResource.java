package org.motechproject.openmrs.resource;

import org.motechproject.openmrs.domain.Attribute;
import org.motechproject.openmrs.domain.AttributeListResult;
import org.motechproject.openmrs.domain.AttributeTypeListResult;
import org.motechproject.openmrs.domain.Person;
import org.motechproject.openmrs.config.Config;

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
     * Returns {@code Attribute.AttributeType} of the attribute matching given uuid. The given {@code config} will
     * be used while performing this action.
     *
     * @param config  the configuration to be used while performing this action
     * @param uuid  the uuid of the AttributeType
     * @return  the attribute type
     */
    Attribute.AttributeType queryPersonAttributeTypeByUuid(Config config, String uuid);

    /**
     * Returns {@code Attribute.AttributeType} of the attribute matching given uuid. The given {@code config} will
     * be used while performing this action.
     *
     * @param config  the configuration to be used while performing this action
     * @param uuid  the uuid of the AttributeType
     * @return  the attribute
     */
    AttributeListResult queryPersonAttributeByPersonUuid(Config config, String uuid);

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
     * Update the preferred attributes of the person with given UUID. The given {@code config} will be used while performing
     * this action.
     *
     * @param config  the configuration to be used while performing this action
     * @param uuid  the UUID of the person
     * @param attribute  the new preferred attribute
     */
    void updatePersonAttribute(Config config, String uuid, Attribute attribute);

    /**
     * Deletes the person with the given UUID from the OpenMRs server. The given {@code config} will be used while
     * performing this action.
     *
     * @param config  the configuration to be used while performing this action
     * @param personUuid  the UUID of the person
     */
    void deletePerson(Config config, String personUuid);

    /**
     * Checking Person's AttributeType format. If format given by OpenMRS server is other than standard Java types
     * (format is internal openMRS object), that means attributes JSON must have hydratedObject field, not value.
     * Person Attribute type can be java.lang.String/Boolean/Integer/Float or Character. Internal OpenMRS formats
     * are e.g. org.openmrs.Concept or org.openmrs.Person
     *
     * @param config  the configuration to be used while performing this action
     * @param person  person that will be created in OpenMRS.
     */
    void checkPersonAttributeTypes(Config config, Person person);
}
