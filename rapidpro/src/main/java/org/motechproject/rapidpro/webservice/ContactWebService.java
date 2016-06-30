package org.motechproject.rapidpro.webservice;

import org.motechproject.rapidpro.exception.WebServiceException;
import org.motechproject.rapidpro.webservice.dto.Contact;

import java.util.UUID;

/*
 * Webservice interface for RapidPro REST API for Contacts.
 */
public interface ContactWebService {

    /**
     * Creates a new contact or updates an existing contact. If the dto has a value for UUID, the contact will be
     * updated. Otherwise, it will create a new contact with those fields.
     *
     * @param contact A representation of a Contact
     * @return {@link Contact} The body of the response from RapidPro
     * @throws WebServiceException
     */
    Contact createOrUpdateContact(Contact contact) throws WebServiceException;

    /**
     * Deletes a contact by its UUID, if it exists
     *
     * @param uuid The rapidpro UUID of the contact.
     * @throws WebServiceException
     */
    void deleteContactByUUID(UUID uuid) throws WebServiceException;

    /**
     * Finds a contact with the corresponding UUID, if it exists.
     *
     * @param uuid The rapidpro UUID of the contact
     * @return {@link Contact} A representation of the contact.
     * @throws WebServiceException
     */
    Contact getContactByUUID(UUID uuid) throws WebServiceException;

    /**
     * Finds a contact with the phone number, if it exists.
     *
     * @param phoneNumber The unique phone number of the Contact
     * @return {@link Contact}
     * @throws WebServiceException
     */
    Contact getContactByPhoneNumber(String phoneNumber) throws WebServiceException;
}
