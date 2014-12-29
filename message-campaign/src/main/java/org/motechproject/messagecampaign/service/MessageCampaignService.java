package org.motechproject.messagecampaign.service;

import org.joda.time.DateTime;
import org.motechproject.messagecampaign.contract.CampaignRequest;
import org.motechproject.messagecampaign.domain.campaign.CampaignRecord;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * \ingroup MessageCampaign
 */
public interface MessageCampaignService {

    String MESSAGE_CAMPAIGNS_JSON_FILENAME = "message-campaigns.json";

    /**
     * Enrolls the external id into the campaign as specified in the request. The enrolled entity will have events raised against it according to the campaign definition.
     *
     * @param enrollRequest
     */
    void enroll(CampaignRequest enrollRequest);

    /**
     * Unenrolls an external from the campaign as specified in the request. The entity will no longer receive events from the campaign.
     *
     * @param externalId   - a client defined id to identify the enrollment
     * @param campaignName - the campaign into which the entity should be enrolled
     */
    void unenroll(String externalId, String campaignName);

    /**
     * Searches and returns the Campaign Enrollment Records as per the criteria in the given CampaignEnrollmentsQuery
     * The query consists of various criteria based on Status, ExternalId and CampaignName of the CampaignEnrollment
     *
     * @param query
     * @return List<CampaignEnrollmentRecord>
     */
    List<CampaignEnrollmentRecord> search(CampaignEnrollmentsQuery query);

    Map<String, List<DateTime>> getCampaignTimings(String externalId, String campaignName, DateTime startDate, DateTime endDate);

    /**
     * Updates existing campaign enrollment with data specified in the request.
     *
     * @param enrollRequest
     * @param enrollmentId
     */
    void updateEnrollment(CampaignRequest enrollRequest, Long enrollmentId);

    /**
     * Unenrolls all campaigns enrollment which match criteria provided by a CampaignEnrollmentsQuery object.
     * @param query
     */
    void stopAll(CampaignEnrollmentsQuery query);

    void saveCampaign(CampaignRecord campaign);

    void deleteCampaign(String campaignName);

    /**
     * Return last message we attempted to send for the given campaign enrollment
     */
    String getLatestCampaignMessage(String campaignName, String externalId);

    /**
     * Return next message that will be sent for given enrollment.
     */
    String getNextCampaignMessage(String campaignName, String externalId);

    CampaignRecord getCampaignRecord(String campaignName);

    List<CampaignRecord> getAllCampaignRecords();

    void campaignCompleted(String externalId, String campaignName);

    void loadCampaigns() throws IOException;
}
