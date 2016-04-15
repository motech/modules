package org.motechproject.openmrs19.service;

import org.joda.time.DateTime;
import org.motechproject.openmrs19.domain.Attribute;
import org.motechproject.openmrs19.domain.Person;

import java.util.List;

/**
 * Interface for handling persons on the OpenMRS server.
 */
public interface OpenMRSPersonService {

    /**
     * Creates the given person on the OpenMRS server. Configuration with the given {@code configName} will be used
     * while performing this action.
     *
     * @param configName  the name of the configuration
     * @param person  the person to be created
     * @return  the created person
     */
    Person createPerson(String configName, Person person);

    /**
     * Creates the given person on the OpenMRS server. The default  configuration will be used while performing
     * this action.
     *
     * @param person  the person to be created
     * @return  the created person
     */
    Person createPerson(Person person);

    /**
     * Creates a person on the OpenMRS server based on the given information. Configuration with the given
     * {@code configName} will be used while performing this action.
     *
     * @param configName  the name of the configuration
     * @param firstName  the person's first name
     * @param lastName  the person's last name
     * @param dateOfBirth  the person's date of birth
     * @param gender  the person's gender
     * @param address  the address of the person
     * @param attributes  a list of attributes for the person
     */
    Person createPerson(String configName, String firstName, String lastName, DateTime dateOfBirth, String gender,
                               String address, List<Attribute> attributes);

    /**
     * Creates a person on the OpenMRS server based on the given information. The default  configuration will be
     * used while performing this action.
     *
     * @param firstName  the person's first name
     * @param lastName  the person's last name
     * @param dateOfBirth  the person's date of birth
     * @param gender  the person's gender
     * @param address  the address of the person
     * @param attributes  a list of attributes for the person
     */
    Person createPerson(String firstName, String lastName, DateTime dateOfBirth, String gender,
                        String address, List<Attribute> attributes);

    /**
     * Updates the person with the information stored in the given {@code person}. Configuration with the given
     * {@code configName} will be used while performing this action.
     *
     * @param configName  the name of the configuration
     * @param person  the person to be used as an update source
     * @return the updated person
     */
    Person updatePerson(String configName, Person person);

    /**
     * Updates the person with the information stored in the given {@code person}. The default  configuration
     * will be used while performing this action.
     *
     * @param person  the person to be used as an update source
     * @return the updated person
     */
    Person updatePerson(Person person);

    /**
     * Deletes the person with the given {@code uuid} from the OpenMRS server. If the person with the given
     * {@code uuid} doesn't exist an error will be logged. Configuration with the given {@code configName} will be used
     * while performing this action.
     *
     * @param configName  the name of the configuration
     * @param uuid  the UUID of the person
     */
    void deletePerson(String configName, String uuid);

    /**
     * Deletes the person with the given {@code uuid} from the OpenMRS server. If the person with the given
     * {@code uuid} doesn't exist an error will be logged. The default  configuration will be used while
     * performing this action.
     *
     * @param uuid  the UUID of the person
     */
    void deletePerson(String uuid);

    /**
     * Returns the person with the given {@code uuid}. Configuration with the given {@code configName} will be used
     * while performing this action.
     *
     * @param configName  the name of the configuration
     * @param uuid  the UUID of the person
     * @return the person with the given UUID, null if the person doesn't exist
     */
    Person getPersonByUuid(String configName, String uuid);

    /**
     * Returns the person with the given {@code uuid}. The default  configuration will be used while performing
     * this action.
     *
     * @param uuid  the UUID of the person
     * @return the person with the given UUID, null if the person doesn't exist
     */
    Person getPersonByUuid(String uuid);
}
