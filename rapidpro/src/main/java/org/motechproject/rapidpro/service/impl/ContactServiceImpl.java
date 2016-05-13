package org.motechproject.rapidpro.service.impl;

import org.motechproject.admin.service.StatusMessageService;
import org.motechproject.rapidpro.exception.NoMappingException;
import org.motechproject.rapidpro.exception.WebServiceException;
import org.motechproject.rapidpro.service.ContactMapperService;
import org.motechproject.rapidpro.service.ContactService;
import org.motechproject.rapidpro.util.ContactUtils;
import org.motechproject.rapidpro.webservice.ContactWebService;
import org.motechproject.rapidpro.webservice.dto.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementaton of {@link ContactService}.
 */

@Service
public class ContactServiceImpl implements ContactService {

    private static final String COMMUNICATION_ERROR = "An error occurred while communicating with Rapidpro: ";
    private static final String WEB_SERVICE_CREATE_UPDATE_FAIL = "Unable to create or update contact." + COMMUNICATION_ERROR;
    private static final String WEB_SERVICE_DELETE_FAIL = "Unable to delete contact." + COMMUNICATION_ERROR;
    private static final String EXTERNAL_ID_NULL = "External ID is null";
    private static final String MAPPING_EXISTS = "Cannot create contact. A contact with this external ID already exists: ";
    private static final String NO_MAPPING = "No mapping for external ID: ";
    private static final String MODULE_NAME = "rapidpro";
    private static final String CREATING_CONTACT = "Creating Contact with external ID: ";
    private static final String UPDATING_CONTACT = "Updating Contact with external ID: ";
    private static final String DELETING_CONTACT = "Deleting Contact with external ID: ";
    private static final String CONTACT_DETAILS = "Contact Details: ";
    private static final String PHONE_EXISTS = "Cannot create contact. A contact with this phone number already exists: ";
    private static final Logger LOGGER = LoggerFactory.getLogger(ContactServiceImpl.class);

    private ContactMapperService contactMapperService;
    private ContactWebService contactWebService;
    private StatusMessageService statusMessageService;

    @Autowired
    public ContactServiceImpl(ContactMapperService contactMapperService, ContactWebService contactWebService, StatusMessageService statusMessageService) {
        this.contactMapperService = contactMapperService;
        this.contactWebService = contactWebService;
        this.statusMessageService = statusMessageService;
    }

    @Override
    public void create(String externalId, String phone, Contact contact) {
        LOGGER.debug(CREATING_CONTACT + externalId);
        if (!externalIdNull(externalId)) {

            if (contactMapperService.exists(externalId)) {
                LOGGER.warn(MAPPING_EXISTS + externalId);
                statusMessageService.warn(MAPPING_EXISTS + externalId, MODULE_NAME);

            } else {
                createContact(externalId, phone, contact);
            }
        } else {
            LOGGER.warn(EXTERNAL_ID_NULL);
        }
    }

    @Override
    public void update(String externalId, Contact contact) {
        LOGGER.debug(UPDATING_CONTACT + externalId);
        try {
            if (!externalIdNull(externalId)) {
                String uuid = contactMapperService.getRapidproUUIDFromExternalId(externalId);
                Contact fromRapidpro = contactWebService.getContactByUUID(uuid);
                ContactUtils.mergeContactFields(contact, fromRapidpro);
                contactWebService.createOrUpdateContact(contact);
            }

        } catch (WebServiceException e) {
            sendWebserviceFailUpdateMessage(e, contact);

        } catch (NoMappingException e) {
            sendNoMappingMessage(externalId);
        }
    }

    @Override
    public void delete(String externalId) {
        LOGGER.debug(DELETING_CONTACT + externalId);
        try {
            if (!externalIdNull(externalId)) {
                String uuid = contactMapperService.getRapidproUUIDFromExternalId(externalId);
                contactWebService.deleteContactByUUID(uuid);
                contactMapperService.delete(externalId);

            } else {
                LOGGER.warn(EXTERNAL_ID_NULL);
            }

        } catch (WebServiceException e) {
            sendWebServiceFailDeleteMessage(e);

        } catch (NoMappingException e) {
            sendNoMappingMessage(externalId);
        }
    }

    @Override
    public Contact findByExternalId(String externalId) {
        LOGGER.debug("Finding Contact with External ID: " + externalId);
        Contact contact = null;

        try {
            if (!externalIdNull(externalId)) {
                String uuid = contactMapperService.getRapidproUUIDFromExternalId(externalId);
                contact = contactWebService.getContactByUUID(uuid);

            } else {
                LOGGER.warn(EXTERNAL_ID_NULL);
            }

        } catch (NoMappingException e) {
            sendNoMappingMessage(externalId);

        } catch (WebServiceException e) {
            LOGGER.warn("ERROR" + e.getMessage());
        }

        return contact;
    }

    private void createContact(String externalId, String phone, Contact contact) {
        try {
            Contact shouldBeNull = contactWebService.getContactByPhoneNumber(phone);

            if (shouldBeNull != null) {
                LOGGER.warn(PHONE_EXISTS + phone);
                statusMessageService.warn(PHONE_EXISTS + phone, MODULE_NAME);

            } else {
                Contact created = contactWebService.createOrUpdateContact(contact);
                contactMapperService.create(externalId, created.getUuid());
            }
        } catch (WebServiceException e) {
            sendWebserviceFailUpdateMessage(e, contact);
        }
    }

    private void sendWebserviceFailUpdateMessage(WebServiceException e, Contact contact) {
        statusMessageService.warn(WEB_SERVICE_CREATE_UPDATE_FAIL + e.getMessage(), MODULE_NAME);
        LOGGER.warn(WEB_SERVICE_CREATE_UPDATE_FAIL + e.getMessage());
        LOGGER.warn(CONTACT_DETAILS + contact.toString());
    }

    private void sendWebServiceFailDeleteMessage(WebServiceException e) {
        statusMessageService.warn(WEB_SERVICE_DELETE_FAIL + e.getMessage(), MODULE_NAME);
        LOGGER.warn(WEB_SERVICE_DELETE_FAIL + e.getMessage());
    }

    private boolean externalIdNull(String externalId) {
        return externalId == null;
    }

    private void sendNoMappingMessage(String externalId) {
        statusMessageService.warn(NO_MAPPING + externalId, MODULE_NAME);
        LOGGER.warn(NO_MAPPING + externalId);
    }
}
