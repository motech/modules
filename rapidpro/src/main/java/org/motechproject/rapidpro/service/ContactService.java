package org.motechproject.rapidpro.service;

import org.motechproject.rapidpro.webservice.dto.Contact;

/**
 * A service for CRUD operations on {@link Contact}. Provides an interface to perform CRUD operations
 * on Rapidpro Contacts, including mapping from an external ID to a Rapidpro UUID and communications over HTTP with
 * Rapidpro.
 */
public interface ContactService {

    /**
     * Creates a new Contact in Rapidpro and a mapping from an external ID to a Rapidpro UUID.
     *
     * @param externalId The external ID used to map to a Rapidpro Contact
     * @param phone      The unique phone number of the Contact
     * @param contact    Object containing the necessary fields to create a new Contact
     */
    void create(String externalId, String phone, Contact contact);

    /**
     * Updates a contact in Rapidpro
     *
     * @param externalId The external ID mapping to a Rapidpro UUID
     * @param contact    Object containing the fields to update a Contact.
     */
    void update(String externalId, Contact contact);

    /**
     * Deletes a contact in Rapidpro
     *
     * @param externalId The external ID mapping to a Rapidpro UUID
     */
    void delete(String externalId);

    /**
     * Finds a contact by its external ID, if it exists.
     *
     * @param externalId The external ID mapping to a Rapidpro UUID.
     * @return {@link Contact}
     */
    Contact findByExternalId(String externalId);

    /**
     * Adds a contact to a group
     * @param externalId The external ID mapping to a Rapidpro contact UUID.
     * @param groupName The name of the Rapidpro group.
     */
    void addToGroup(String externalId, String groupName);

    /**
     * Removes a contact from a group.
     * @param externalId The external ID mapping to a Rapidpro contact UUID.
     * @param groupName The name of the Rapidpro group.
     */
    void removeFromGroup(String externalId, String groupName);
}
