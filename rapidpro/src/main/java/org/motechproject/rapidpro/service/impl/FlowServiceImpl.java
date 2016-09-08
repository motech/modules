package org.motechproject.rapidpro.service.impl;


import org.motechproject.rapidpro.event.publisher.EventPublisher;
import org.motechproject.rapidpro.exception.WebServiceException;
import org.motechproject.rapidpro.service.ContactService;
import org.motechproject.rapidpro.service.FlowService;
import org.motechproject.rapidpro.webservice.FlowRunWebService;
import org.motechproject.rapidpro.webservice.FlowWebService;
import org.motechproject.rapidpro.webservice.GroupWebService;
import org.motechproject.rapidpro.webservice.dto.Contact;
import org.motechproject.rapidpro.webservice.dto.Flow;
import org.motechproject.rapidpro.webservice.dto.FlowRunRequest;
import org.motechproject.rapidpro.webservice.dto.FlowRunResponse;
import org.motechproject.rapidpro.webservice.dto.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service("rapidproFlowService")
public class FlowServiceImpl implements FlowService {
    private static final String NO_CONTACT = "No contact exists with external ID: ";
    private static final String NO_FLOW_NAME = "Flow does not exist with name: ";
    private static final String NO_FLOW_UUID = "Flow does not exist with UUID: ";
    private static final String NO_GROUP = "No group exists with name: ";
    private static final String STARTING_FLOW_CONTACT = "Starting flow for contact with external ID: ";
    private static final String STARTING_FLOW_GROUP = "Starting flow for group: ";
    private static final String ERROR_RAPIDPRO = "Error communicating with RapidPro: ";

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowServiceImpl.class);

    private FlowWebService flowWebService;
    private ContactService contactService;
    private FlowRunWebService flowRunWebService;
    private EventPublisher eventPublisher;
    private GroupWebService groupWebService;

    @Autowired
    public FlowServiceImpl(FlowWebService flowWebService, ContactService contactService,
                           FlowRunWebService flowRunWebService, EventPublisher eventPublisher, GroupWebService groupWebService) {
        this.flowWebService = flowWebService;
        this.contactService = contactService;
        this.flowRunWebService = flowRunWebService;
        this.eventPublisher = eventPublisher;
        this.groupWebService = groupWebService;
    }

    @Override
    public void startFlowForContact(String flowName, String contactExtId, boolean restart, Map<String, String> extra) {
        LOGGER.debug(STARTING_FLOW_CONTACT + contactExtId);
        try {
            Flow flow = flowWebService.getFlow(flowName);
            if (flow != null) {
                validateRequestContact(flow, contactExtId, restart, extra);

            } else {
                String message = NO_FLOW_NAME + flowName;
                LOGGER.warn(message);
                eventPublisher.publishFlowFailContact(message, contactExtId, flowName, restart, extra);
            }
        } catch (WebServiceException e) {
            String message = ERROR_RAPIDPRO + e.getMessage();
            LOGGER.warn(message);
            eventPublisher.publishFlowFailContact(ERROR_RAPIDPRO + e.getMessage(), contactExtId, flowName, restart, extra);
        }
    }

    @Override
    public void startFlowForContact(UUID flowUUID, String contactExtId, boolean restartParticipants, Map<String, String> extra) {
        LOGGER.debug(STARTING_FLOW_CONTACT + contactExtId);
        try {
            Flow flow = flowWebService.getFlow(flowUUID);
            if (flow != null) {
                validateRequestContact(flow, contactExtId, restartParticipants, extra);

            } else {
                String message = NO_FLOW_UUID + flowUUID;
                LOGGER.warn(message);
                eventPublisher.publishFlowFailContact(message, contactExtId, flowUUID, restartParticipants, extra);
            }
        } catch (WebServiceException e) {
            String message = ERROR_RAPIDPRO + e.getMessage();
            LOGGER.warn(message);
            eventPublisher.publishFlowFailContact(message, contactExtId, flowUUID, restartParticipants, extra);
        }
    }

    @Override
    public void startFlowForGroup(String flowName, String groupName, boolean restartParticipants, Map<String, String> extra) {
        LOGGER.debug(STARTING_FLOW_GROUP + groupName);
        try {
            Flow flow = flowWebService.getFlow(flowName);
            if (flow != null) {
                validateRequestGroup(flow, groupName, restartParticipants, extra);

            } else {
                String message = NO_FLOW_NAME + flowName;
                LOGGER.warn(message);
                eventPublisher.publishFlowFailGroup(message, groupName, flowName, restartParticipants, extra);
            }

        } catch (WebServiceException e) {
            String message = ERROR_RAPIDPRO + e.getMessage();
            LOGGER.warn(message);
            eventPublisher.publishFlowFailGroup(message, groupName, flowName, restartParticipants, extra);
        }
    }


    @Override
    public void startFlowForGroup(UUID flowUUID, String groupName, boolean restartParticipants, Map<String, String> extra) {
        LOGGER.debug(STARTING_FLOW_GROUP + groupName);
        try {
            Flow flow = flowWebService.getFlow(flowUUID);
            if (flow != null) {
                validateRequestGroup(flow, groupName, restartParticipants, extra);

            } else {
                String message = NO_FLOW_UUID + flowUUID;
                LOGGER.warn(message);
                eventPublisher.publishFlowFailGroup(message, groupName, flowUUID, restartParticipants, extra);
            }

        } catch (WebServiceException e) {
            String message = ERROR_RAPIDPRO + e.getMessage();
            LOGGER.warn(message);
            eventPublisher.publishFlowFailGroup(message, groupName, flowUUID, restartParticipants, extra);
        }
    }

    private void validateRequestGroup(Flow flow, String groupName, boolean restart, Map<String, String> extra) throws WebServiceException {
        Group group = groupWebService.getGroupByName(groupName);

        if (group != null) {
            initiateRequest(flow, group, restart, extra);

        } else {
            String message = NO_GROUP + groupName;
            LOGGER.warn(message);
            eventPublisher.publishFlowFailGroup(message, groupName, flow.getName(), restart, extra);
        }
    }


    private void validateRequestContact(Flow flow, String contactExtId, boolean restart, Map<String, String> extra) throws WebServiceException {
        Contact contact = contactService.findByExternalId(contactExtId);

        if (contact != null) {
            initiateRequest(flow, contact, contactExtId, restart, extra);

        } else {
            String message = NO_CONTACT + contactExtId;
            LOGGER.warn(message);
            eventPublisher.publishFlowFailContact(message, contactExtId, flow.getName(), restart, extra);
        }
    }

    private void initiateRequest(Flow flow, Contact contact, String contactExtId, boolean restart, Map<String, String> extra) throws WebServiceException {
        FlowRunRequest.FlowRunRequestBuilder builder = new FlowRunRequest.FlowRunRequestBuilder(flow.getUuid())
                .addContact(contact.getUuid())
                .setRestartParticipants(restart)
                .setExtra(extra);
        FlowRunRequest flowRunRequest = builder.build();

        List<FlowRunResponse> flowRunResponses = flowRunWebService.startFlowRuns(flowRunRequest);
        eventPublisher.publishFlowStartedContact(contactExtId, contact, flowRunRequest, flowRunResponses.get(0));
    }

    private void initiateRequest(Flow flow, Group group, boolean restart, Map<String, String> extra) throws WebServiceException {
        FlowRunRequest.FlowRunRequestBuilder builder = new FlowRunRequest.FlowRunRequestBuilder(flow.getUuid())
                .addGroup(group.getUuid())
                .setExtra(extra)
                .setRestartParticipants(restart);
        FlowRunRequest flowRunRequest = builder.build();

        List<FlowRunResponse> flowRunResponses = flowRunWebService.startFlowRuns(flowRunRequest);
        eventPublisher.publishFlowStartedGroup(flow.getName(), group, flowRunRequest, flowRunResponses);
    }
}
