package org.motechproject.openmrs19.service;

import org.joda.time.DateTime;
import org.motechproject.openmrs19.domain.OpenMRSAttribute;
import org.motechproject.openmrs19.domain.OpenMRSPerson;

import java.util.List;

/**
 * Interface for handling persons on the OpenMRS server.
 */
public interface OpenMRSPersonService {

    /**
     * Creates the given person on the OpenMRS server.
     *
     * @param person  the person to be created
     * @return  the created person
     */
    OpenMRSPerson createPerson(OpenMRSPerson person);

    /**
     * Creates a person on the OpenMRS server based on the given information.
     *
     * @param firstName  the person's first name
     * @param lastName  the person's last name
     * @param dateOfBirth  the person's date of birth
     * @param gender  the person's gender
     * @param address  the address of the person
     * @param attributes  a list of attributes for the person
     */
    OpenMRSPerson createPerson(String firstName, String lastName, DateTime dateOfBirth, String gender,
                               String address, List<OpenMRSAttribute> attributes);

    /**
     * Updates the person with the information stored in the given {@code person}.
     *
     * @param person  the person to be used as an update source
     * @return the updated person
     */
    OpenMRSPerson updatePerson(OpenMRSPerson person);

    /**
     * Deletes the person with the given {@code uuid} from the OpenMRS server. If the person with the given
     * {@code uuid} doesn't exist an error will be logged.
     *
     * @param uuid  the UUID of the person
     */
    void deletePerson(String uuid);

    /**
     * Returns the person with the given {@code uuid}.
     *
     * @param uuid  the UUID of the person
     * @return the person with the given UUID, null if the person doesn't exist
     */
    OpenMRSPerson getPersonByUuid(String uuid);
}
