package org.motechproject.rapidpro.event.publisher.impl;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.rapidpro.constant.EventParameters;
import org.motechproject.rapidpro.constant.EventSubjects;
import org.motechproject.rapidpro.event.publisher.EventPublisher;
import org.motechproject.rapidpro.util.WebHookParser;
import org.motechproject.rapidpro.webservice.dto.Contact;
import org.motechproject.rapidpro.webservice.dto.FlowRunRequest;
import org.motechproject.rapidpro.webservice.dto.FlowRunResponse;
import org.motechproject.rapidpro.webservice.dto.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Implementation of {@link org.motechproject.rapidpro.event.publisher.EventPublisher}
 */

@Component
public class EventPublisherImpl implements EventPublisher {

    @Autowired
    private EventRelay eventRelay;

    @Override
    public void publishContactCreated(String externalId, Contact contact) {
        Map<String, Object> params = createContactParams(externalId, contact);
        MotechEvent event = new MotechEvent(EventSubjects.CONTACT_CREATED, params);
        eventRelay.sendEventMessage(event);
    }

    @Override
    public void publishContactUpdated(String externalId, Contact contact) {
        Map<String, Object> params = createContactParams(externalId, contact);
        MotechEvent event = new MotechEvent(EventSubjects.CONTACT_UPDATED, params);
        eventRelay.sendEventMessage(event);
    }

    @Override
    public void publishContactDeleted(String externalId, UUID contactUUID) {
        Map<String, Object> params = new HashMap<>();
        params.put(EventParameters.EXTERNAL_ID, externalId);
        params.put(EventParameters.CONTACT_UUID, contactUUID);
        MotechEvent event = new MotechEvent(EventSubjects.CONTACT_DELETED, params);
        eventRelay.sendEventMessage(event);
    }

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

    @Override
    public void publishFlowStartedContact(String externalId, Contact contact, FlowRunRequest flowRunRequest, FlowRunResponse flowRunResponse) {
        Map<String, Object> params = new HashMap<>();
        params.put(EventParameters.EXTERNAL_ID, externalId);
        params.put(EventParameters.CONTACT_UUID, contact.getUuid());
        params.put(EventParameters.CONTACT_NAME, contact.getName());
        params.put(EventParameters.URNS, contact.getUrns());
        params.put(EventParameters.FLOW_UUID, flowRunRequest.getFlowUUID());
        params.put(EventParameters.RESTART_PARTICIPANTS, flowRunRequest.isRestartParticipants());
        params.put(EventParameters.FLOW, flowRunResponse.getFlow());
        params.put(EventParameters.FLOW_CREATED_ON, flowRunResponse.getCreatedOn());
        params.put(EventParameters.FLOW_EXPIRED_ON, flowRunResponse.getExpiredOn());
        params.put(EventParameters.FLOW_EXPIRES_ON, flowRunResponse.getExpiresOn());
        params.put(EventParameters.RUN, flowRunResponse.getRun());
        MotechEvent event = new MotechEvent(EventSubjects.FLOW_STARTED_CONTACT, params);
        eventRelay.sendEventMessage(event);
    }

    @Override
    public void publishFlowFailContact(String error, String contactExtId, String flowName, boolean restart, Map<String, String> extra) {
        Map<String, Object> params = createFlowFailContactParams(error, contactExtId, restart, extra);
        params.put(EventParameters.FLOW_NAME, flowName);
        MotechEvent event = new MotechEvent(EventSubjects.FLOW_FAIL_CONTACT_NAME, params);
        eventRelay.sendEventMessage(event);
    }

    @Override
    public void publishFlowFailContact(String error, String contactExtId, UUID flowUUID, boolean restart, Map<String, String> extra) {
        Map<String, Object> params = createFlowFailContactParams(error, contactExtId, restart, extra);
        params.put(EventParameters.FLOW_UUID, flowUUID);
        MotechEvent event = new MotechEvent(EventSubjects.FLOW_FAIL_CONTACT_UUID, params);
        eventRelay.sendEventMessage(event);
    }

    @Override
    public void publishFlowStartedGroup(String flowName, Group group, FlowRunRequest flowRunRequest, List<FlowRunResponse> flowRunResponses) {
        Map<String, Object> params = new HashMap<>();
        List<UUID> contactUUIDs = new ArrayList<>();
        for (FlowRunResponse flowRunResponse : flowRunResponses) {
            contactUUIDs.add(flowRunResponse.getContact());
        }
        params.put(EventParameters.FLOW_NAME, flowName);
        params.put(EventParameters.FLOW_UUID, flowRunRequest.getFlowUUID());
        params.put(EventParameters.GROUP_NAME, group.getName());
        params.put(EventParameters.GROUP_UUID, group.getUuid());
        params.put(EventParameters.RESTART_PARTICIPANTS, flowRunRequest.isRestartParticipants());
        params.put(EventParameters.CONTACT_UUIDS, contactUUIDs);
        MotechEvent event = new MotechEvent(EventSubjects.FLOW_STARTED_GROUP, params);
        eventRelay.sendEventMessage(event);
    }

    @Override
    public void publishFlowFailGroup(String error, String groupName, String flowName, boolean restart, Map<String, String> extra) {
        Map<String, Object> params = createFlowFailGroupParams(error, groupName, restart, extra);
        params.put(EventParameters.FLOW_NAME, flowName);
        MotechEvent event = new MotechEvent(EventSubjects.FLOW_STARTED_GROUP, params);
        eventRelay.sendEventMessage(event);
    }

    @Override
    public void publishFlowFailGroup(String error, String groupName, UUID flowUUID, boolean restart, Map<String, String> extra) {
        Map<String, Object> params = createFlowFailGroupParams(error, groupName, restart, extra);
        params.put(EventParameters.FLOW_UUID, flowUUID);
        MotechEvent event = new MotechEvent(EventSubjects.FLOW_STARTED_GROUP, params);
        eventRelay.sendEventMessage(event);
    }

    @Override
    public void publishWebHookEvent(Map<String, String[]> requestParams) {
        MotechEvent event = WebHookParser.parse(requestParams);
        eventRelay.sendEventMessage(event);
    }

    private Map<String, Object> createAddedOrRemovedGroupParams(String externalId, Contact contact, Group group) {
        Map<String, Object> params = createContactParams(externalId, contact);
        params.put(EventParameters.GROUP_NAME, group.getName());
        params.put(EventParameters.GROUP_UUID, group.getUuid());
        return params;
    }

    private Map<String, Object> createFlowFailContactParams(String error, String contactExtId, boolean restart, Map<String, String> extra) {
        Map<String, Object> params = new HashMap<>();
        params.put(EventParameters.ERROR_MESSAGE, error);
        params.put(EventParameters.EXTERNAL_ID, contactExtId);
        params.put(EventParameters.RESTART_PARTICIPANTS, restart);
        params.put(EventParameters.EXTRA, extra);
        return params;
    }

    private Map<String, Object> createFlowFailGroupParams(String error, String groupName, boolean restart, Map<String, String> extra) {
        Map<String, Object> params = new HashMap<>();
        params.put(EventParameters.ERROR_MESSAGE, error);
        params.put(EventParameters.GROUP_NAME, groupName);
        params.put(EventParameters.RESTART_PARTICIPANTS, restart);
        params.put(EventParameters.EXTRA, extra);
        return params;
    }

    private Map<String, Object> createContactParams(String externalId, Contact contact) {
        Map<String, Object> params = new HashMap<>();
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
        return params;
    }
}
