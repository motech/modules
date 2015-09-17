package org.motechproject.messagecampaign.service;

import org.joda.time.DateTime;
import org.motechproject.messagecampaign.contract.CampaignRequest;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollment;
import org.motechproject.messagecampaign.domain.campaign.CampaignRecord;
import org.motechproject.messagecampaign.domain.campaign.CampaignMessageRecord;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * The <code>MessageCampaignService</code> allows the management of the message campaigns and the campaign enrollments.
 * It is exposed as OSGi service.
 */
public interface MessageCampaignService {

    String MESSAGE_CAMPAIGNS_JSON_FILENAME = "message-campaigns.json";

    /**
     * Enrolls the external ID into the campaign as specified in the request. The enrolled entity will have events raised
     * against it, according to the campaign definition.
     *
     * @param enrollRequest the enrollment request
     */
    void enroll(CampaignRequest enrollRequest);

    /**
     * Unenrolls the external ID from the campaign as specified in the request. The entity will no longer receive events
     * from the campaign.
     *
     * @param externalId a client defined ID to identify the enrollment
     * @param campaignName the campaign from which the entity should be unenrolled
     */
    void unenroll(String externalId, String campaignName);

    /**
     * Searches and returns the {@link CampaignEnrollmentRecord}s as per the criteria in the given {@link CampaignEnrollmentsQuery}
     * The query consists of various criteria based on Status, ExternalId and CampaignName of the CampaignEnrollment
     *
     * @param query the query containing search criteria
     * @return the list of enrollment records matching provided criteria
     */
    List<CampaignEnrollmentRecord> search(CampaignEnrollmentsQuery query);

    /**
     * Gets the complete schedule of the messages to deliver for the given campaign name and the given external ID of
     * the enrollment. The schedule contains messages sent after startDate and before endDate. It is returned as a map,
     * indexed by message names.
     *
     * @param externalId external ID of the enrollment
     * @param campaignName the name of the message campaign
     * @param startDate the beginning of the time window, determining the included messages
     * @param endDate the ending of the time window, determining the included messages
     * @return
     */
    Map<String, List<DateTime>> getCampaignTimings(String externalId, String campaignName, DateTime startDate, DateTime endDate);

    /**
     * Updates existing campaign enrollment with data specified in the request.
     *
     * @param enrollRequest the enrollment request holding the data to update
     * @param enrollmentId the ID of the enrollment
     */
    void updateEnrollment(CampaignRequest enrollRequest, Long enrollmentId);

    /**
     * Unenrolls all campaign enrollments which match criteria provided in the {@link CampaignEnrollmentsQuery}.
     *
     * @param query the query containing criteria of the records to unenroll
     */
    void stopAll(CampaignEnrollmentsQuery query);

    /**
     * Unenrolls and deletes all campaign enrollments which match criteria provided in the {@link CampaignEnrollmentsQuery}.
     *
     * @param query the query containing criteria of the records to unenroll and delete
     */
    void stopAll(CampaignEnrollmentsQuery query, boolean deleteEnrollments);

    /**
     * Creates a new message campaign, provided a one with such name does not exist yet.
     *
     * @param campaign the campaign to create
     */
    void saveCampaign(CampaignRecord campaign);

    /**
     * Deletes a message campaign of the given name and stops all the enrollments to this campaign.
     *
     * @param campaignName the name of the campaign to delete
     */
    void deleteCampaign(String campaignName);

    /**
     * Return last message we attempted to send for the given campaign enrollment.
     *
     * @param campaignName the name of the campaign
     * @param externalId the external ID of the enrollment
     * @return the message key of the latest messsage
     */
    String getLatestCampaignMessage(String campaignName, String externalId);

    /**
     * Return next message that will be sent for the given campaign enrollment.
     *
     * @param campaignName the name of the campaign
     * @param externalId the external ID of the enrollment
     * @return the message key of the next message
     */
    String getNextCampaignMessage(String campaignName, String externalId);

    /**
     * Returns {@link CampaignRecord} of the provided name.
     *
     * @param campaignName the name of the campaign
     * @return campaign record of the given name
     */
    CampaignRecord getCampaignRecord(String campaignName);

    /**
     * Returns all {@link CampaignRecord}s.
     *
     * @return all campaign records.
     */
    List<CampaignRecord> getAllCampaignRecords();

    /**
     * @see #unenroll(String, String)
     */
    @Deprecated
    void campaignCompleted(String externalId, String campaignName);

    /**
     * Loads message campaigns from the module settings and creates or updates necessary records,
     * based on the loaded definition.
     *
     * @throws IOException in case of problems reading the module settings
     */
    void loadCampaigns() throws IOException;

    /**
     * Updates all active enrollments of the message campaign with the given ID.
     *
     * @param campaignId the ID of the message campaign
     * @throws IllegalArgumentException in case the message campaign of the given ID does not exist
     */
    void updateEnrollments(Long campaignId);

    /**
     * Unschedules all the jobs set up to send campaign messages for enrollments for the given campaign.
     *
     * @param campaignMessageRecord one of the campaign's message to unschedule jobs for
     */
    void unscheduleMessageJob(CampaignMessageRecord campaignMessageRecord);

    /**
     * Reschedules all the jobs set up to send campaign messages for enrollments for the given campaign.
     *
     * @param campaignMessageRecordId one of the campaign's message ID to reschedule jobs for
     */
    void rescheduleMessageJob(Long campaignMessageRecordId);

    /**
     * Schedules jobs for the given campaign enrollment.
     *
     * @param enrollment the enrollment to schedule the jobs for
     */
    void scheduleJobsForEnrollment(CampaignEnrollment enrollment);

    /**
     * Unschedules all the jobs for the given campaign enrollment.
     *
     * @param enrollment the enrollment to unschedule jobs for
     */
    void unscheduleJobsForEnrollment(CampaignEnrollment enrollment);
}
