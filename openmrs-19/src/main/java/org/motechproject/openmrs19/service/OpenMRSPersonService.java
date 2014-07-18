package org.motechproject.openmrs19.service;

import org.joda.time.DateTime;
import org.motechproject.openmrs19.domain.OpenMRSAttribute;
import org.motechproject.openmrs19.domain.OpenMRSPerson;

import java.util.List;

public interface OpenMRSPersonService {

    /**
     * Persists a person object.
     * 
     * @param person  the person to be saved 
     * @throws OpenMrsException if the person object violated constraints in the implementing module
     */
    OpenMRSPerson addPerson(OpenMRSPerson person);

    /**
     * Creates and persists a person object from field values.
     *
     * @param personId  the ID of the person
     * @param firstName  the person's first name
     * @param lastName  the person's last name
     * @param dateOfBirth  the person's date of birth
     * @param gender  the person's gender
     * @param address  the address of the person
     * @param attributes  a list of attributes for the person
     * @throws OpenMrsException if the person object violated constraints in the implementing module
     */
    OpenMRSPerson addPerson(String personId, String firstName, String lastName, DateTime dateOfBirth, String gender,
            String address, List<OpenMRSAttribute> attributes);

    /**
     * Updates a person object.
     *
     * @param person  the person to update
     */
    OpenMRSPerson updatePerson(OpenMRSPerson person);

    /**
     * Removes a person from storage.
     *
     * @param person  the person to remove
     */
    void removePerson(OpenMRSPerson person);

    /**
     * Finds a list of persons by a particular ID.
     *
     * @param personId The ID of the person to search for
     * @return a list of all persons of given ID, of a type from the implementing module
     */
    List<OpenMRSPerson> findByPersonId(String personId);

}
