package org.motechproject.rapidpro.service;


import java.util.Map;
import java.util.UUID;

/**
 * A service for starting flow runs in RapidPro.
 */
public interface FlowService {

    /**
     * Starts a flow for a contact using the flow's name.
     * @param flowName The name of the flow.
     * @param contactExternalId The external ID mapping to a contact UUID.
     * @param restartParticipants Indicates if flow runs should be restarted.
     * @param extra Extra payload for the flow run.
     */
    void startFlowForContact(String flowName, String contactExternalId, boolean restartParticipants, Map<String, String> extra);

    /**
     * Starts a flow for a contact using the flow's UUID.
     * @param flowUUID The UUID of the flow.
     * @param contactExternalId The external ID mapping to a contact UUID.
     * @param restartParticipants Indicates if flow runs should be restarted.
     * @param extra Extra payload for the flow run.
     */
    void startFlowForContact (UUID flowUUID, String contactExternalId, boolean restartParticipants, Map<String, String> extra);

    /**
     * Starts a flow for a group using the flow's name.
     * @param flowName The name of the flow.
     * @param groupName The name of the group.
     * @param restartParticipants Indicates if flow runs should be restarted.
     * @param extra Extra payload for the flow run.
     */
    void startFlowForGroup(String flowName, String groupName, boolean restartParticipants, Map<String, String> extra);

    /**
     * Starts a flow for a group using the flow's UUID.
     * @param flowUUID The name of the flow.
     * @param groupName The name of the group.
     * @param restartParticipants Indicates if flow runs should be restarted.
     * @param extra Extra payload for the flow run.
     */
    void startFlowForGroup(UUID flowUUID, String groupName, boolean restartParticipants, Map<String, String> extra);
}
