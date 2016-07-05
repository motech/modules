package org.motechproject.rapidpro.event;


import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.rapidpro.constant.EventParameters;
import org.motechproject.rapidpro.constant.EventSubjects;
import org.motechproject.rapidpro.service.ContactService;
import org.motechproject.rapidpro.util.ContactUtils;
import org.motechproject.rapidpro.webservice.dto.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Handles all incoming events relating to {@link Contact}.
 */
@Component
public class ContactEventHandler {

    private static final String HANDLE_CREATE_EVENT = "Handling create contact event: ";
    private static final String HANDLE_UPDATE_EVENT = "Handling update contact event: ";
    private static final String HANDLE_DELETE_EVENT = "Handling delete contact event";
    private static final String HANDLE_ADD_TO_GROUP_EVENT = "Handling add contact to group event";
    private static final String HANDLE_REMOVE_FROM_GROUP = "Handling remove from group event";

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactEventHandler.class);

    @Autowired
    private ContactService contactService;

    /**
     * Handles events with the create contact event subject
     *
     * @param event {@link MotechEvent}
     */
    @MotechListener(subjects = EventSubjects.CREATE_CONTACT)
    public void handleCreate(MotechEvent event) {
        LOGGER.info(HANDLE_CREATE_EVENT + event.toString());
        Map<String, Object> params = event.getParameters();
        String externalId = (String) params.get(EventParameters.EXTERNAL_ID);
        String phone = (String) params.get(EventParameters.PHONE);
        Contact contact = ContactUtils.toContactFromParams(params);
        contactService.create(externalId, phone, contact);
    }

    /**
     * Handles events with the update contact event subject
     *
     * @param event {@link MotechEvent}
     */
    @MotechListener(subjects = EventSubjects.UPDATE_CONTACT)
    public void handleUpdate(MotechEvent event) {
        LOGGER.info(HANDLE_UPDATE_EVENT + event.toString());
        Map<String, Object> params = event.getParameters();
        String externalId = (String) params.get(EventParameters.EXTERNAL_ID);
        Contact updated = ContactUtils.toContactFromParams(params);
        contactService.update(externalId, updated);
    }

    /**
     * Handles events with the delete contact event subject.
     *
     * @param event {@link MotechEvent}
     */
    @MotechListener(subjects = EventSubjects.DELETE_CONTACT)
    public void handleDelete(MotechEvent event) {
        LOGGER.info(HANDLE_DELETE_EVENT);
        String externalId = (String) event.getParameters().get(EventParameters.EXTERNAL_ID);
        contactService.delete(externalId);
    }

    /**
     * Handles events with the add to group event subject.
     * @param event {@link MotechEvent}
     */
    @MotechListener(subjects = EventSubjects.ADD_TO_GROUP)
    public void handleAddToGroup(MotechEvent event) {
        LOGGER.info(HANDLE_ADD_TO_GROUP_EVENT);
        Map<String, Object> params = event.getParameters();
        String externalId = (String) params.get(EventParameters.EXTERNAL_ID);
        String groupName = (String) params.get(EventParameters.GROUP_NAME);
        contactService.addToGroup(externalId, groupName);
    }

    /**
     * Handles events with the remove from group event subject.
     * @param event {@link MotechEvent}
     */
    @MotechListener(subjects = EventSubjects.REMOVE_FROM_GROUP)
    public void handleRemoveFromGroup(MotechEvent event) {
        LOGGER.info(HANDLE_REMOVE_FROM_GROUP);
        Map<String, Object> params = event.getParameters();
        String externalId = (String) params.get(EventParameters.EXTERNAL_ID);
        String groupName = (String) params.get(EventParameters.GROUP_NAME);
        contactService.removeFromGroup(externalId, groupName);
    }

}
