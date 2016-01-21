package org.motechproject.messagecampaign.scheduler;

import org.joda.time.DateTime;
import org.motechproject.commons.date.model.Time;
import org.motechproject.event.MotechEvent;
import org.motechproject.messagecampaign.EventKeys;
import org.motechproject.messagecampaign.builder.SchedulerPayloadBuilder;
import org.motechproject.messagecampaign.dao.CampaignRecordService;
import org.motechproject.messagecampaign.domain.campaign.Campaign;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollment;
import org.motechproject.messagecampaign.domain.campaign.CampaignMessage;
import org.motechproject.messagecampaign.exception.CampaignEnrollmentException;
import org.motechproject.scheduler.contract.JobId;
import org.motechproject.scheduler.contract.RunOnceSchedulableJob;
import org.motechproject.scheduler.service.MotechSchedulerService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract class for handling campaign scheduling and unscheduling.
 *
 * @param <M> Type of {@link CampaignMessage}
 * @param <C> Type of {@link Campaign}
 */
public abstract class CampaignSchedulerService<M extends CampaignMessage, C extends Campaign<M>> {
    private MotechSchedulerService schedulerService;
    private CampaignRecordService campaignRecordService;
    private JobIdFactory jobIdFactory;

    protected CampaignSchedulerService(MotechSchedulerService schedulerService, CampaignRecordService campaignRecordService) {
        this.schedulerService = schedulerService;
        this.campaignRecordService = campaignRecordService;
        jobIdFactory = new JobIdFactory();
    }

    /**
     * Schedules all the necessary jobs for the given {@link CampaignEnrollment}.
     * The scheduled jobs include firing message events, as well as signaling the end
     * of the campaign.
     *
     * @param enrollment the enrollment to schedule jobs for
     */
    public void start(CampaignEnrollment enrollment) {
        C campaign = (C) getCampaignRecordService().findByName(enrollment.getCampaignName()).toCampaign();
        for (M message : campaign.getMessages()) {
            scheduleMessageJob(enrollment, campaign, message);
        }

        scheduleEndOfCampaignEvent(campaign, enrollment);
    }

    /**
     * Unschedules all the remaining jobs for the given {@link CampaignEnrollment}.
     * The unscheduling includes the end of the campaign event and all the message events.
     *
     * @param enrollment the enrollment to unschedule jobs for
     */
    public void stop(CampaignEnrollment enrollment) {
        schedulerService.safeUnscheduleRunOnceJob(EventKeys.CAMPAIGN_COMPLETED,
                jobIdFactory.campaignCompletedJobIdFor(enrollment.getCampaignName(), enrollment.getExternalId()));
        unscheduleMessageJobs(enrollment);
    }

    /**
     * Reschedules a single job, responsible for firing the event for the given {@link CampaignMessage}. The new job
     * is scheduled based on the provided {@link Campaign} definition and {@link CampaignEnrollment}.
     *
     * @param enrollment the enrollment to reschedule job for
     * @param campaign the new campaign definition
     * @param message the message to reschedule job for
     */
    public void rescheduleMessageJob(CampaignEnrollment enrollment, Campaign campaign, CampaignMessage message) {
        unscheduleMessageJob(enrollment, message);
        scheduleMessageJob(enrollment, (C) campaign, (M) message);
        rescheduleEndOfCampaignEvent((C) campaign, enrollment);
    }

    /**
     * Gets the complete schedule of the messages to deliver for the given {@link CampaignEnrollment}.
     * The schedule contains messages sent after startDate and before endDate. It is returned as a map, indexed by message names.
     *
     * @param startDate the beginning of the time window, determining the included messages
     * @param endDate the ending of the time window, determining the included messages
     * @param enrollment the enrollment to include the messages from
     * @return the schedule of messages, returned as a map, indexed by message keys
     */
    public Map<String, List<DateTime>> getCampaignTimings(DateTime startDate, DateTime endDate, CampaignEnrollment enrollment) {
        C campaign = (C) getCampaignRecordService().findByName(enrollment.getCampaignName()).toCampaign();
        return getCampaignTimings(startDate, endDate, enrollment, campaign);
    }

    /**
     * Unschedules a job, responsible for sending the provided {@link CampaignMessage}, belonging to the provided
     * {@link CampaignMessage}.
     *
     * @param enrollment enrollment the unscheduled job belongs to
     * @param campaignMessage the message to unschedule
     */
    public abstract void unscheduleMessageJob(CampaignEnrollment enrollment, CampaignMessage campaignMessage);

    /**
     * Gets the ID, used in the MOTECH Scheduler to schedule jobs.
     *
     * @param messageKey the message key, which is a part of the built ID
     * @param externalId the external ID, which is a part of the built ID
     * @param campaingName the campaign name, which is a part of the built ID
     * @return job ID
     */
    public abstract JobId getJobId(String messageKey, String externalId, String campaingName);

    /**
     * Unschedules all jobs, responsible for firing message events for the provided {@link CampaignEnrollment}.
     *
     * @param enrollment the enrollment to unschedule message jobs for
     */
    protected abstract void unscheduleMessageJobs(CampaignEnrollment enrollment);

    /**
     * Gets the complete schedule of the messages to deliver for the given {@link Campaign}.
     * The schedule contains messages sent after startDate and before endDate. It is returned as a map, indexed by message names.
     *
     * @param startDate the beginning of the time window, determining the included messages
     * @param endDate the ending of the time window, determining the included messages
     * @param enrollment the enrollment to include the messages from
     * @param campaign the campaign definition containing the messages
     * @return the schedule of messages, returned as a map, indexed by message keys
     */
    protected Map<String, List<DateTime>> getCampaignTimings(DateTime startDate, DateTime endDate, CampaignEnrollment enrollment,
                                                             C campaign) {
        Map<String, List<DateTime>> messageTimingsMap = new HashMap<>();
        for (M message : campaign.getMessages()) {
            String externalJobIdPrefix = messageJobIdFor(message.getMessageKey(), enrollment.getExternalId(), enrollment.getCampaignName());
            List<DateTime> dates = schedulerService.getScheduledJobTimingsWithPrefix(EventKeys.SEND_MESSAGE, externalJobIdPrefix, startDate, endDate);

            messageTimingsMap.put(message.getName(), dates);
        }
        return messageTimingsMap;
    }

    /**
     * Returns the date and time of the last message in the provided {@link CampaignEnrollment}, of the given {@link Campaign}.
     *
     * @param campaign the campaign definition
     * @param enrollment the enrollment to return the end date for
     * @return the date and time of the last message
     */
    protected abstract DateTime campaignEndDate(C campaign, CampaignEnrollment enrollment);

    /**
     * Schedules a job, responsible for firing the event for the provided message, as a part of the given {@link CampaignEnrollment},
     * belonging to the provided campaign.
     *
     * @param enrollment the enrollment the message belongs to
     * @param campaign the campaign definition
     * @param message the message to schedule
     */
    protected abstract void scheduleMessageJob(CampaignEnrollment enrollment, C campaign, M message);

    /**
     * Returns the delivery time, for the given {@link CampaignMessage}, that belongs to the provided {@link CampaignEnrollment}.
     * It will return the time from enrollment if provided. Otherwise it will take the default time from the message definition.
     *
     * @param enrollment the enrollment the message belongs to
     * @param message the message to fetch the delivery time for
     * @return delivery time for the message
     * @throws CampaignEnrollmentException if the delivery time has been provided neither in the enrollment nor in the message definition
     */
    protected Time deliverTimeFor(CampaignEnrollment enrollment, CampaignMessage message) {
        Time deliveryTime = enrollment.getDeliverTime() != null ? enrollment.getDeliverTime() : message.getStartTime();

        if (deliveryTime == null) {
            throw new CampaignEnrollmentException(String.format("Cannot enroll %s for message campaign %s - Start time not defined for campaign. Define it in campaign-message.json or at enrollment time", enrollment.getExternalId(), message.getName()));
        }
        return deliveryTime;
    }

    /**
     * Prepares the job parameters, that will be included in the MOTECH Event, sent as a part of
     * the message campaign.
     *
     * @param messageKey the message key to include in the parameters
     * @param enrollment the enrollment to prepare the parameters for
     * @return a map of job parameters
     */
    protected Map<String, Object> jobParams(String messageKey, CampaignEnrollment enrollment) {
        Campaign campaign = getCampaignRecordService().findByName(enrollment.getCampaignName()).toCampaign();
        return new SchedulerPayloadBuilder()
                .withJobId(messageJobIdFor(messageKey, enrollment.getExternalId(), enrollment.getCampaignName()))
                .withCampaignName(campaign.getName())
                .withMessageKey(messageKey)
                .withExternalId(enrollment.getExternalId())
                .payload();
    }

    /**
     * Generates ID for the message job, based on the provided parameters.
     *
     * @param messageKey the message key
     * @param externalId external ID
     * @param campaignName the campaign name
     * @return ID for the message job
     */
    protected String messageJobIdFor(String messageKey, String externalId, String campaignName) {
        return jobIdFactory.getMessageJobIdFor(campaignName, externalId, messageKey);
    }

    /**
     * Takes the messaging schedule as a map and finds the date and time of the last message.
     *
     * @param timingsMap messaging schedule as a map
     * @return the date and time of the last message
     */
    protected DateTime findLastDateTime(Map<String, List<DateTime>> timingsMap) {
        DateTime finalFireTime = null;
        for (List<DateTime> dateTimeList : timingsMap.values()) {
            for (DateTime dateTime : dateTimeList) {
                if (finalFireTime == null || dateTime.isAfter(finalFireTime)) {
                    finalFireTime = dateTime;
                }
            }
        }
        return finalFireTime;
    }

    /**
     * Schedules a single job, that fires when the last message of the campaign is sent.
     *
     * @param campaign the campaign definition
     * @param enrollment the campaign enrollment to schedule the job for
     */
    protected void scheduleEndOfCampaignEvent(C campaign, CampaignEnrollment enrollment) {
        DateTime endDate = campaignEndDate(campaign, enrollment);

        if (endDate != null && endDate.isAfterNow()) {
            String campaignName = campaign.getName();
            String externalId = enrollment.getExternalId();

            Map<String, Object> params = new SchedulerPayloadBuilder()
                .withExternalId(externalId)
                .withCampaignName(campaignName)
                .withJobId(jobIdFactory.campaignCompletedJobIdFor(campaignName, externalId))
                .payload();

            MotechEvent endOfCampaignEvent = new MotechEvent(EventKeys.CAMPAIGN_COMPLETED, params);

            schedulerService.scheduleRunOnceJob(new RunOnceSchedulableJob(endOfCampaignEvent, endDate));
        } else if (endDate != null && endDate.isBeforeNow()) {
            throw new IllegalArgumentException(
                    String.format("No messages scheduled for enrollment with ID %s for campaign %s, last message was scheduled in the past(%s)",
                            enrollment.getExternalId(), enrollment.getCampaignName(), endDate.toString()));
        }
    }

    private void rescheduleEndOfCampaignEvent(C campaign, CampaignEnrollment enrollment) {
        schedulerService.safeUnscheduleRunOnceJob(EventKeys.CAMPAIGN_COMPLETED,
                jobIdFactory.campaignCompletedJobIdFor(enrollment.getCampaignName(), enrollment.getExternalId()));
        scheduleEndOfCampaignEvent(campaign, enrollment);
    }

    public MotechSchedulerService getSchedulerService() {
        return schedulerService;
    }

    public CampaignRecordService getCampaignRecordService() {
        return campaignRecordService;
    }
}

