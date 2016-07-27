package org.motechproject.rapidpro.event.publisher.impl;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.rapidpro.constant.EventParameters;
import org.motechproject.rapidpro.constant.EventSubjects;
import org.motechproject.rapidpro.event.publisher.EventPublisher;
import org.motechproject.rapidpro.webservice.dto.Contact;
import org.motechproject.rapidpro.webservice.dto.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of {@link org.motechproject.rapidpro.event.publisher.EventPublisher}
 */

@Component
public class EventPublisherImpl implements EventPublisher {

    @Autowired
    private EventRelay eventRelay;

    @Override
    public void publishContactAddedToGroup(String externalId, Contact contact, Group group) {
        Map<String, Object> params = createAddedOrRemovedGroupParams(externalId, contact, group);
        MotechEvent event = new MotechEvent(EventSubjects.ADDED_TO_GROUP, params);
        eventRelay.sendEventMessage(event);
    }

    @Override
    public void publishContactRemovedFromGroup(String externalId, Contact contact, Group group) {
        Map<String, Object> params = createAddedOrRemovedGroupParams(externalId, contact, group);
        MotechEvent event = new MotechEvent(EventSubjects.REMOVED_FROM_GROUP, params);
        eventRelay.sendEventMessage(event);
    }

    private Map<String, Object> createAddedOrRemovedGroupParams(String externalId, Contact contact, Group group) {
        Map<String, Object> params = new HashMap<>();

        /*Contact parameters*/
        params.put(EventParameters.EXTERNAL_ID, externalId);
        params.put(EventParameters.CONTACT_UUID, contact.getUuid().toString());
        params.put(EventParameters.CONTACT_NAME, contact.getName());
        params.put(EventParameters.LANGUAGE, contact.getLanguage());
        params.put(EventParameters.URNS, contact.getUrns());
        params.put(EventParameters.CONTACT_GROUP_UUIDS, contact.getGroupUUIDs());
        params.put(EventParameters.FIELDS, contact.getFields());
        params.put(EventParameters.BLOCKED, contact.isBlocked());
        params.put(EventParameters.FAILED, contact.isFailed());
        params.put(EventParameters.MODIFIED_ON, contact.getModifiedOn());

        /*Group parameters*/
        params.put(EventParameters.GROUP_NAME, group.getName());
        params.put(EventParameters.GROUP_UUID, group.getUuid());
        return params;
    }
}
