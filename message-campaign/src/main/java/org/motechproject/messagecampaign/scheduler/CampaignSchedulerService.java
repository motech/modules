package org.motechproject.messagecampaign.scheduler;

import org.joda.time.DateTime;
import org.motechproject.commons.date.model.Time;
import org.motechproject.event.MotechEvent;
import org.motechproject.messagecampaign.EventKeys;
import org.motechproject.messagecampaign.builder.SchedulerPayloadBuilder;
import org.motechproject.messagecampaign.dao.CampaignRecordService;
import org.motechproject.messagecampaign.domain.campaign.Campaign;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollment;
import org.motechproject.messagecampaign.domain.message.CampaignMessage;
import org.motechproject.messagecampaign.scheduler.exception.CampaignEnrollmentException;
import org.motechproject.scheduler.contract.RunOnceSchedulableJob;
import org.motechproject.scheduler.service.MotechSchedulerService;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract class for handling campaign (un)scheduling
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

    public void start(CampaignEnrollment enrollment) {
        C campaign = (C) getCampaignRecordService().findByName(enrollment.getCampaignName()).build();
        for (M message : campaign.getMessages()) {
            scheduleMessageJob(enrollment, campaign, message);
        }

        scheduleEndOfCampaignEvent(campaign, enrollment);
    }

    public void stop(CampaignEnrollment enrollment) {
        schedulerService.safeUnscheduleRunOnceJob(EventKeys.CAMPAIGN_COMPLETED,
                jobIdFactory.campaignCompletedJobIdFor(enrollment.getCampaignName(), enrollment.getExternalId()));
        unscheduleMessageJobs(enrollment);
    }

    protected abstract void unscheduleMessageJobs(CampaignEnrollment enrollment);

    public Map<String, List<DateTime>> getCampaignTimings(DateTime startDate, DateTime endDate, CampaignEnrollment enrollment) {
        C campaign = (C) getCampaignRecordService().findByName(enrollment.getCampaignName()).build();
        return getCampaignTimings(startDate, endDate, enrollment, campaign);
    }

    protected Map<String, List<DateTime>> getCampaignTimings(DateTime startDate, DateTime endDate, CampaignEnrollment enrollment,
                                                             C campaign) {
        Map<String, List<DateTime>> messageTimingsMap = new HashMap<>();
        for (M message : campaign.getMessages()) {
            String externalJobIdPrefix = messageJobIdFor(message.getMessageKey(), enrollment.getExternalId(), enrollment.getCampaignName());
            List<DateTime> dates = convertToDateTimeList(schedulerService.getScheduledJobTimingsWithPrefix(EventKeys.SEND_MESSAGE, externalJobIdPrefix, startDate.toDate(), endDate.toDate()));

            messageTimingsMap.put(message.getName(), dates);
        }
        return messageTimingsMap;
    }

    protected abstract DateTime campaignEndDate(C campaign, CampaignEnrollment enrollment);

    protected abstract void scheduleMessageJob(CampaignEnrollment enrollment, C campaign, M message);

    protected Time deliverTimeFor(CampaignEnrollment enrollment, CampaignMessage message) {
        Time deliveryTime = enrollment.getDeliverTime() != null ? enrollment.getDeliverTime() : message.getStartTime();

        if (deliveryTime == null) {
            throw new CampaignEnrollmentException(String.format("Cannot enroll %s for message campaign %s - Start time not defined for campaign. Define it in campaign-message.json or at enrollment time", enrollment.getExternalId(), message.getName()));
        }
        return deliveryTime;
    }

    protected Map<String, Object> jobParams(String messageKey, CampaignEnrollment enrollment) {
        Campaign campaign = getCampaignRecordService().findByName(enrollment.getCampaignName()).build();
        return new SchedulerPayloadBuilder()
                .withJobId(messageJobIdFor(messageKey, enrollment.getExternalId(), enrollment.getCampaignName()))
                .withCampaignName(campaign.getName())
                .withMessageKey(messageKey)
                .withExternalId(enrollment.getExternalId())
                .payload();
    }

    protected String messageJobIdFor(String messageKey, String externalId, String campaignName) {
        return jobIdFactory.getMessageJobIdFor(campaignName, externalId, messageKey);
    }

    public MotechSchedulerService getSchedulerService() {
        return schedulerService;
    }

    public CampaignRecordService getCampaignRecordService() {
        return campaignRecordService;
    }

    private List<DateTime> convertToDateTimeList(final List<Date> dates) {
        List<DateTime> list = new ArrayList<>(dates.size());

        for (Date date : dates) {
            list.add(new DateTime(date));
        }

        return list;
    }

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

    protected void scheduleEndOfCampaignEvent(C campaign, CampaignEnrollment enrollment) {
        DateTime endDate = campaignEndDate(campaign, enrollment);

        if (endDate != null) {
            String campaignName = campaign.getName();
            String externalId = enrollment.getExternalId();

            Map<String, Object> params = new SchedulerPayloadBuilder()
                .withExternalId(externalId)
                .withCampaignName(campaignName)
                .withJobId(jobIdFactory.campaignCompletedJobIdFor(campaignName, externalId))
                .payload();

            MotechEvent endOfCampaignEvent = new MotechEvent(EventKeys.CAMPAIGN_COMPLETED, params);

            schedulerService.scheduleRunOnceJob(new RunOnceSchedulableJob(endOfCampaignEvent, endDate.toDate()));
        }
    }
}

