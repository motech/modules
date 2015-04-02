package org.motechproject.openmrs19.service;

import org.joda.time.DateTime;
import org.motechproject.openmrs19.domain.OpenMRSAttribute;
import org.motechproject.openmrs19.domain.OpenMRSPerson;

import java.util.List;

public interface OpenMRSPersonService {

    /**
     * Creates the given person on the OpenMRS server.
     *
     * @param person  the person to be created
     * @return  the created person
     */
    OpenMRSPerson createPerson(OpenMRSPerson person);

    /**
     * Creates and persists a person object from field values.
     *
     * @param firstName  the person's first name
     * @param lastName  the person's last name
     * @param dateOfBirth  the person's date of birth
     * @param gender  the person's gender
     * @param address  the address of the person
     * @param attributes  a list of attributes for the person
     */
    OpenMRSPerson addPerson(String firstName, String lastName, DateTime dateOfBirth, String gender,
            String address, List<OpenMRSAttribute> attributes);

    /**
     * Updates a person object.
     *
     * @param person  the person to update
     */
    OpenMRSPerson updatePerson(OpenMRSPerson person);

    /**
     * Deletes person with the given UUID from the OpenMRS server.
     *
     * @param uuid  the UUID of the person
     */
    void deletePerson(String uuid);

    /**
     * Finds person by a particular ID.
     *
     * @param personId The ID of the person to search for
     * @return a list of all persons of given ID, of a type from the implementing module
     */
    OpenMRSPerson getByUuid(String personId);

}
