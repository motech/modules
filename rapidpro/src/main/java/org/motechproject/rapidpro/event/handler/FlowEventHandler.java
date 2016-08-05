package org.motechproject.rapidpro.event.handler;


import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.rapidpro.constant.EventParameters;
import org.motechproject.rapidpro.constant.EventSubjects;
import org.motechproject.rapidpro.service.FlowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

/**
 * Handles all incoming events related to RapidPro Flows.
 */
@Component
public class FlowEventHandler {
    private static final String HANDLING_CONTACT_FLOW = "Handling event for contact flow run";
    private static final String HANDLING_GROUP_FLOW = "Handling event for group flow run";
    private static final String UNSUPPORTED_EVENT_SUBJECT= "Unsupported event subject: ";

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowEventHandler.class);

    @Autowired
    FlowService flowService;

    /**
     * Handles flow run requests for individual contacts.
     * @param event {@link MotechEvent}
     */
    @MotechListener(subjects = {EventSubjects.START_FLOW_CONTACT_UUID, EventSubjects.START_FLOW_CONTACT_NAME})
    public void handleStartContactFlow(MotechEvent event) {
        LOGGER.info(HANDLING_CONTACT_FLOW);
        String contactExternalId = (String) event.getParameters().get(EventParameters.EXTERNAL_ID);
        Boolean restartParticipants = (Boolean) event.getParameters().get(EventParameters.RESTART_PARTICIPANTS);
        boolean restartToBoolean = restartParticipants == null || restartParticipants;
        Map<String, String> extra = (Map<String, String>) event.getParameters().get(EventParameters.EXTRA);

        switch (event.getSubject()) {

            case EventSubjects.START_FLOW_CONTACT_NAME:
                String flowName = (String) event.getParameters().get(EventParameters.FLOW_NAME);
                flowService.startFlowForContact(flowName, contactExternalId, restartToBoolean, extra);
                break;

            case EventSubjects.START_FLOW_CONTACT_UUID:
                String flowUUIDString = (String) event.getParameters().get(EventParameters.FLOW_UUID);
                UUID flowUUID = UUID.fromString(flowUUIDString);
                flowService.startFlowForContact(flowUUID, contactExternalId, restartToBoolean, extra);
                break;

            default:
                String message = UNSUPPORTED_EVENT_SUBJECT + event.getSubject();
                LOGGER.error(message);
                throw new RuntimeException(message);
        }
    }

    /**
     * Handles flow run requests for groups.
     * @param event {@link MotechEvent}
     */
    @MotechListener(subjects = {EventSubjects.START_FLOW_GROUP_NAME, EventSubjects.START_FLOW_GROUP_UUID})
    public void handleStartGroupFlow(MotechEvent event) {
        LOGGER.info(HANDLING_GROUP_FLOW);
        Boolean restartParticipants = (Boolean) event.getParameters().get(EventParameters.RESTART_PARTICIPANTS);
        boolean restartToBoolean = restartParticipants == null || restartParticipants;
        Map<String, String> extra = (Map<String, String>) event.getParameters().get(EventParameters.EXTRA);
        String groupName = (String) event.getParameters().get(EventParameters.GROUP_NAME);

        switch (event.getSubject()) {

            case EventSubjects.START_FLOW_GROUP_NAME:
                String flowName = (String) event.getParameters().get(EventParameters.FLOW_NAME);
                flowService.startFlowForGroup(flowName, groupName, restartToBoolean, extra);
                break;

            case EventSubjects.START_FLOW_GROUP_UUID:
                String flowUUIDString = (String) event.getParameters().get(EventParameters.FLOW_UUID);
                UUID flowUUID = UUID.fromString(flowUUIDString);
                flowService.startFlowForGroup(flowUUID, groupName, restartToBoolean, extra);
                break;

            default:
                String message = UNSUPPORTED_EVENT_SUBJECT + event.getSubject();
                LOGGER.error(message);
                throw new RuntimeException(message);
        }
    }
}
