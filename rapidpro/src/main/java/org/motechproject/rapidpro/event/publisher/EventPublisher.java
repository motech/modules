package org.motechproject.rapidpro.event.publisher;

import org.motechproject.rapidpro.webservice.dto.Contact;
import org.motechproject.rapidpro.webservice.dto.FlowRunRequest;
import org.motechproject.rapidpro.webservice.dto.FlowRunResponse;
import org.motechproject.rapidpro.webservice.dto.Group;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Publishes events occurring in the RapidPro module.
 */
public interface EventPublisher {

    /**
     * Publishes an {@link org.motechproject.event.MotechEvent} with subject
     * org.motechproject.rapidpro.contact-created
     * @param externalId The external ID of the created contact
     * @param contact The details of the newly created contact
     */
    void publishContactCreated(String externalId, Contact contact);

    /**
     * Publishes an {@link org.motechproject.event.MotechEvent} with subject
     * org.motechproject.rapidpro.contact-updated
     * @param externalId The external ID of the updated contact
     * @param contact The details of the updated contact
     */
    void publishContactUpdated(String externalId, Contact contact);

    /**
     * Publishes an {@link org.motechproject.event.MotechEvent} with subject
     * org.motechproject.rapidpro.contact-deleted
     * @param externalId The external ID of the deleted contact
     * @param contactUUID The UUID of the deleted contact
     */
    void publishContactDeleted(String externalId, UUID contactUUID);

    /**
     * Publishes an {@link org.motechproject.event.MotechEvent} with subject
     * org.motechproject.rapidpro.contact-added-to-group
     *
     * @param externalId The external ID of the contact added to the group
     * @param contact    The contact added to the group.
     * @param group      The group the contact was added to.
     */
    void publishContactAddedToGroup(String externalId, Contact contact, Group group);

    /**
     * Publishes an {@link org.motechproject.event.MotechEvent} with subject
     * org.motechproject.rapidpro.contact-removed-from-group
     *
     * @param externalId The external ID of the contact removed from the group
     * @param contact    The contact removed from the group
     * @param group      The group the contact was removed from.
     */
    void publishContactRemovedFromGroup(String externalId, Contact contact, Group group);

    /**
     * Publishes an {@link org.motechproject.event.MotechEvent} with subject
     * org.motechproject.rapidpro.flow-started-for-contact
     *
     * @param externalId      The external ID mapping to a contact's UUID
     * @param contact         The contact that will recieve a flow run
     * @param flowRunRequest  The request made for the flow run
     * @param flowRunResponse The response from RapidPro
     */
    void publishFlowStartedContact(String externalId, Contact contact, FlowRunRequest flowRunRequest, FlowRunResponse flowRunResponse);

    /**
     * Publishes an {@link org.motechproject.event.MotechEvent} with subject
     * org.motechproject.rapidpro.flow-started-for-group
     *
     * @param flowName         The name of the flow to be started
     * @param group            The name of the group of contacts that will recieve the flow run
     * @param flowRunRequest   The request made for the flow run
     * @param flowRunResponses The response from RapidPro
     */
    void publishFlowStartedGroup(String flowName, Group group, FlowRunRequest flowRunRequest, List<FlowRunResponse> flowRunResponses);

    /**
     * Publishes an {@link org.motechproject.event.MotechEvent} with subject
     * org.motechproject.rapidpro.flow-fail-group-name
     *
     * @param error     Message describing the nature of the error
     * @param groupName The name of the group of contacts
     * @param flowName  The name of the flow
     * @param restart   Indicates if flow runs should be restarted
     * @param extra     Extra payload for the flow run.
     */
    void publishFlowFailGroup(String error, String groupName, String flowName, boolean restart, Map<String, String> extra);

    /**
     * Publishes an {@link org.motechproject.event.MotechEvent} with subject
     * org.motechproject.rapidpro.flow-fail-group-name
     *
     * @param error     Message describing the nature of the error
     * @param groupName The name of the group of contacts
     * @param flowUUID  The UUID of the flow
     * @param restart   Indicates if flow runs should be restarted
     * @param extra     Extra payload for the flow run.
     */
    void publishFlowFailGroup(String error, String groupName, UUID flowUUID, boolean restart, Map<String, String> extra);

    /**
     * Publishes an {@link org.motechproject.event.MotechEvent} with subject
     * org.motechproject.rapidpro.flow-fail-contact-name
     *
     * @param error        Message describing the nature of the error
     * @param contactExtId The external ID mapping to a contact's UUID
     * @param flowName     The name of the flow
     * @param restart      Indicates if flow runs should be restarted
     * @param extra        Extra payload for the flow run.
     */
    void publishFlowFailContact(String error, String contactExtId, String flowName, boolean restart, Map<String, String> extra);

    /**
     * Publishes an {@link org.motechproject.event.MotechEvent} with subject
     * org.motechproject.rapidpro.flow-fail-contact-name
     *
     * @param error        Message describing the nature of the error
     * @param contactExtId The external ID mapping to a contact's UUID
     * @param flowUUID     The UUID of the flow
     * @param restart      Indicates if flow runs should be restarted
     * @param extra        Extra payload for the flow run.
     */
    void publishFlowFailContact(String error, String contactExtId, UUID flowUUID, boolean restart, Map<String, String> extra);

    /**
     * Publishes an {@link org.motechproject.event.MotechEvent} corresponding to the received web hook from
     * RapidPro.
     *
     * @param requestParams The parameter map from an HTTP POST request made by RapidPro.
     */
    void publishWebHookEvent(Map<String, String[]> requestParams);

}
