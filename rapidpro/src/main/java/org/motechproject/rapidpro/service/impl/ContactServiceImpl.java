package org.motechproject.rapidpro.service.impl;

import org.motechproject.admin.service.StatusMessageService;
import org.motechproject.rapidpro.event.publisher.EventPublisher;
import org.motechproject.rapidpro.exception.NoMappingException;
import org.motechproject.rapidpro.exception.WebServiceException;
import org.motechproject.rapidpro.service.ContactMapperService;
import org.motechproject.rapidpro.service.ContactService;
import org.motechproject.rapidpro.util.ContactUtils;
import org.motechproject.rapidpro.webservice.ContactWebService;
import org.motechproject.rapidpro.webservice.GroupWebService;
import org.motechproject.rapidpro.webservice.dto.Contact;
import org.motechproject.rapidpro.webservice.dto.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.UUID;

/**
 * Implementation of {@link ContactService}.
 */

@Service("rapidproContactService")
public class ContactServiceImpl implements ContactService {

    private static final String COMMUNICATION_ERROR = "An error occurred while communicating with Rapidpro: ";
    private static final String WEB_SERVICE_CREATE_UPDATE_FAIL = "Unable to create or update contact." + COMMUNICATION_ERROR;
    private static final String WEB_SERVICE_DELETE_FAIL = "Unable to delete contact." + COMMUNICATION_ERROR;
    private static final String WEB_SERVICE_FIND_FAIL = "Unable to find contact with external ID: ";
    private static final String EXTERNAL_ID_NULL = "External ID is null";
    private static final String MAPPING_EXISTS = "Cannot create contact. A contact with this external ID already exists: ";
    private static final String NO_MAPPING = "No mapping for external ID: ";
    private static final String MODULE_NAME = "rapidpro";
    private static final String CREATING_CONTACT = "Creating Contact with external ID: ";
    private static final String UPDATING_CONTACT = "Updating Contact with external ID: ";
    private static final String DELETING_CONTACT = "Deleting Contact with external ID: ";
    private static final String FINDING_EXTERNAL_ID = "Finding Contact with External ID: ";
    private static final String EXTERNAL_ID_NOT_EXISTS = "Contact with external ID: %s does not exist";
    private static final String ADD_TO_GROUP_NAME = "Adding contact with external ID: {} to group {}";
    private static final String GROUP_NOT_EXIST = "Group with name: %s does not exist";
    private static final String WEBSERVICE_FAIL_ADD_GROUP = "Unable to add contact with external ID {} to group {}." + COMMUNICATION_ERROR;
    private static final String WEBSERVICE_FAIL_REMOVE_GROUP = "Unable to remove contact with external ID %s from group %s." + COMMUNICATION_ERROR;
    private static final String PHONE_EXISTS = "Cannot create contact. A contact with this phone number already exists: ";

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactServiceImpl.class);

    private ContactMapperService contactMapperService;
    private ContactWebService contactWebService;
    private StatusMessageService statusMessageService;
    private GroupWebService groupWebService;
    private EventPublisher eventPublisher;

    @Autowired
    public ContactServiceImpl(ContactMapperService contactMapperService, ContactWebService contactWebService,
                              StatusMessageService statusMessageService, GroupWebService groupWebService, EventPublisher eventPublisher) {
        this.contactMapperService = contactMapperService;
        this.contactWebService = contactWebService;
        this.statusMessageService = statusMessageService;
        this.groupWebService = groupWebService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void create(String externalId, String phone, Contact contact) {
        LOGGER.debug(CREATING_CONTACT + externalId);
        if (externalId != null) {

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
            if (externalId != null) {
                UUID uuid = contactMapperService.getRapidproUUIDFromExternalId(externalId);
                Contact fromRapidpro = contactWebService.getContactByUUID(uuid);
                ContactUtils.mergeContactFields(contact, fromRapidpro);
                contactWebService.createOrUpdateContact(contact);
                eventPublisher.publishContactUpdated(externalId, fromRapidpro);
            }

        } catch (WebServiceException e) {
            sendWebserviceFailUpdateMessage(e);

        } catch (NoMappingException e) {
            sendNoMappingMessage(externalId);
        }
    }


    @Override
    public void delete(String externalId) {
        LOGGER.debug(DELETING_CONTACT + externalId);
        try {
            if (externalId != null) {
                UUID uuid = contactMapperService.getRapidproUUIDFromExternalId(externalId);
                contactWebService.deleteContactByUUID(uuid);
                contactMapperService.delete(externalId);
                eventPublisher.publishContactDeleted(externalId, uuid);

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
        LOGGER.debug(FINDING_EXTERNAL_ID + externalId);
        Contact contact = null;

        try {
            if (externalId != null) {
                UUID uuid = contactMapperService.getRapidproUUIDFromExternalId(externalId);
                contact = contactWebService.getContactByUUID(uuid);

            } else {
                LOGGER.warn(EXTERNAL_ID_NULL);
            }

        } catch (NoMappingException e) {
            sendNoMappingMessage(externalId);

        } catch (WebServiceException e) {
            sendWebserviceFailFindMessage(externalId, e);
        }

        return contact;
    }

    @Override
    public void addToGroup(String externalId, String groupName) {
        LOGGER.debug(ADD_TO_GROUP_NAME, externalId, groupName);
        try {
            Group group = groupWebService.getGroupByName(groupName);

            if (group != null) {
                Contact contact = findByExternalId(externalId);

                if (contact != null) {
                    contact.getGroupUUIDs().add(group.getUuid());
                    Contact updated = contactWebService.createOrUpdateContact(contact);
                    eventPublisher.publishContactAddedToGroup(externalId, updated, group);

                } else {
                    sendContactDoesNotExistMessage(externalId);
                }

            } else {
                sendGroupDoesNotExistMessage(groupName);
            }

        } catch (WebServiceException e) {
            sendWebserviceFailAddGroupMessage(e, externalId, groupName);
        }
    }

    @Override
    public void removeFromGroup(String externalId, String groupName) {
        LOGGER.debug(ADD_TO_GROUP_NAME, externalId, groupName);
        try {
            Group group = groupWebService.getGroupByName(groupName);

            if (group != null) {
                Contact contact = findByExternalId(externalId);

                if (contact != null) {
                    contact.getGroupUUIDs().remove(group.getUuid());
                    Contact updated = contactWebService.createOrUpdateContact(contact);
                    eventPublisher.publishContactRemovedFromGroup(externalId, updated, group);

                } else {
                    sendContactDoesNotExistMessage(externalId);
                }

            } else {
                sendGroupDoesNotExistMessage(groupName);
            }

        } catch (WebServiceException e) {
            sendWebserviceFailRemoveGroupMessage(e, externalId, groupName);
        }
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
                eventPublisher.publishContactCreated(externalId, created);
            }
        } catch (WebServiceException e) {
            sendWebserviceFailUpdateMessage(e);
        }
    }

    private void sendWebserviceFailUpdateMessage(WebServiceException e) {
        statusMessageService.warn(WEB_SERVICE_CREATE_UPDATE_FAIL + e.getMessage(), MODULE_NAME);
        LOGGER.warn(WEB_SERVICE_CREATE_UPDATE_FAIL + e.getMessage(), e);
    }

    private void sendWebServiceFailDeleteMessage(WebServiceException e) {
        statusMessageService.warn(WEB_SERVICE_DELETE_FAIL + e.getMessage(), MODULE_NAME);
        LOGGER.warn(WEB_SERVICE_DELETE_FAIL + e.getMessage(), e);
    }

    private void sendNoMappingMessage(String externalId) {
        statusMessageService.warn(NO_MAPPING + externalId, MODULE_NAME);
        LOGGER.warn(NO_MAPPING + externalId);
    }

    private void sendWebserviceFailFindMessage(String externalID, WebServiceException e) {
        statusMessageService.warn(WEB_SERVICE_FIND_FAIL + externalID + "." + COMMUNICATION_ERROR + e.getMessage(), MODULE_NAME);
        LOGGER.warn(WEB_SERVICE_FIND_FAIL + externalID + "." + COMMUNICATION_ERROR + e.getMessage(), e);
    }

    private void sendContactDoesNotExistMessage(String externalID) {
        String message = String.format(EXTERNAL_ID_NOT_EXISTS, externalID);
        statusMessageService.warn(message, MODULE_NAME);
        LOGGER.warn(message);
    }

    private void sendGroupDoesNotExistMessage(String groupName) {
        String message = String.format(GROUP_NOT_EXIST, groupName);
        statusMessageService.warn(message, MODULE_NAME);
        LOGGER.warn(message);
    }

    private void sendWebserviceFailAddGroupMessage(WebServiceException e, String externalId, String groupName) {
        String message = String.format(WEBSERVICE_FAIL_ADD_GROUP, externalId, groupName);
        statusMessageService.warn(message + e.getMessage(), MODULE_NAME);
        LOGGER.warn(message, e);
    }

    private void sendWebserviceFailRemoveGroupMessage(WebServiceException e, String externalId, String groupName) {
        String message = String.format(WEBSERVICE_FAIL_REMOVE_GROUP, externalId, groupName);
        statusMessageService.warn(message + e.getMessage(), MODULE_NAME);
        LOGGER.warn(message, e);
    }

}
