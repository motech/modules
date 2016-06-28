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
        String externalId = (String) event.getParameters().get(EventParameters.EXTERNAL_ID);
        contactService.delete(externalId);
    }
}
