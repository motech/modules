package org.motechproject.rapidpro.webservice.impl;

import org.codehaus.jackson.type.TypeReference;
import org.motechproject.rapidpro.exception.JsonUtilException;
import org.motechproject.rapidpro.exception.RapidProClientException;
import org.motechproject.rapidpro.exception.WebServiceException;
import org.motechproject.rapidpro.util.JsonUtils;
import org.motechproject.rapidpro.webservice.ContactWebService;
import org.motechproject.rapidpro.webservice.MediaFormat;
import org.motechproject.rapidpro.webservice.RapidProHttpClient;
import org.motechproject.rapidpro.webservice.dto.Contact;
import org.motechproject.rapidpro.webservice.dto.PaginatedResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Implementation of {@link ContactWebService}
 */

@Service
public class ContactWebServiceImpl implements ContactWebService {

    private static final String CONTACTS_ENDPOINT = "/contacts";
    private static final String ERROR_CREATING_OR_UPDATING = "Error creating or updating contact contact: ";
    private static final String ERROR_DELETING = "Error Deleting Contact with UUID: ";
    private static final String ERROR_RETREIVING_UUID = "Error retrieving contact with UUID: ";
    private static final String ERROR_RETREIVING_PHONE = "Error retrieving contact with phone number: ";
    private static final String CREATING = "Creating or updating contact: ";
    private static final String DELETING = "Deleting contact with UUID: ";
    private static final String FINDING = "Finding contact with UUID: ";
    private static final String UUID = "uuid";
    private static final String PHONE = "phone";

    private static final TypeReference CONTACT_TYPE_REF = new TypeReference<Contact>() {
    };
    private static final TypeReference PAGED_RESPONSE_CONTACT_TYPE_REF = new TypeReference<PaginatedResponse<Contact>>() {
    };
    private static final Logger LOGGER = LoggerFactory.getLogger(ContactWebServiceImpl.class);

    @Autowired
    private RapidProHttpClient client;

    @Override
    public Contact createOrUpdateContact(Contact contact) throws WebServiceException {
        InputStream response = null;
        try {
            LOGGER.debug(CREATING + contact.toString());
            byte[] body = JsonUtils.toByteArray(contact);
            response = client.executePost(CONTACTS_ENDPOINT, body, MediaFormat.JSON, MediaFormat.JSON);
            return (Contact) JsonUtils.toObject(response, CONTACT_TYPE_REF);

        } catch (RapidProClientException | JsonUtilException e) {
            throw new WebServiceException(ERROR_CREATING_OR_UPDATING + contact.getName(), e);

        } finally {
            closeInputStream(response);
        }
    }

    @Override
    public void deleteContactByUUID(UUID uuid) throws WebServiceException {
        try {
            LOGGER.debug(DELETING + uuid);
            Map<String, String> params = new HashMap<>();
            params.put(UUID, uuid.toString());
            client.executeDelete(CONTACTS_ENDPOINT, MediaFormat.JSON, params);
        } catch (RapidProClientException e) {
            throw new WebServiceException(ERROR_DELETING + uuid, e);
        }
    }

    @Override
    public Contact getContactByUUID(UUID uuid) throws WebServiceException {
        try {
            LOGGER.debug(FINDING + uuid);
            Map<String, String> params = new HashMap<>();
            params.put(UUID, uuid.toString());
            return getWithParams(params);
        } catch (RapidProClientException | JsonUtilException e) {
            throw new WebServiceException(ERROR_RETREIVING_UUID + uuid, e);
        }
    }

    @Override
    public Contact getContactByPhoneNumber(String phoneNumber) throws WebServiceException {
        try {
            LOGGER.debug("Finding contact with phone number: " + phoneNumber);
            Map<String, String> params = new HashMap<>();
            params.put(PHONE, phoneNumber);
            return getWithParams(params);
        } catch (RapidProClientException | JsonUtilException e) {
            throw new WebServiceException(ERROR_RETREIVING_PHONE + phoneNumber, e);
        }
    }

    private Contact getWithParams(Map<String, String> params) throws WebServiceException, RapidProClientException, JsonUtilException {
        InputStream response = client.executeGet(CONTACTS_ENDPOINT, MediaFormat.JSON, params);
        PaginatedResponse<Contact> paginatedResponse = (PaginatedResponse<Contact>) JsonUtils.toObject(response, PAGED_RESPONSE_CONTACT_TYPE_REF);
        if (paginatedResponse.getResults().size() == 0) {
            return null;

        } else if (paginatedResponse.getResults().size() == 1) {
            return paginatedResponse.getResults().get(0);

        } else {
            throw new WebServiceException("Query Returned more than one contact.Parameters:  " + params.toString());
        }
    }

    private void closeInputStream(InputStream is) {
        try {
            is.close();
        } catch (IOException e) { }
    }
}
