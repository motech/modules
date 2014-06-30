package org.motechproject.messagecampaign.scheduler;

import org.joda.time.DateTime;
import org.motechproject.commons.date.model.Time;
import org.motechproject.messagecampaign.EventKeys;
import org.motechproject.messagecampaign.builder.SchedulerPayloadBuilder;
import org.motechproject.messagecampaign.domain.campaign.Campaign;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollment;
import org.motechproject.messagecampaign.domain.message.CampaignMessage;
import org.motechproject.messagecampaign.scheduler.exception.CampaignEnrollmentException;
import org.motechproject.messagecampaign.dao.CampaignRecordService;
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
            scheduleMessageJob(enrollment, message);
        }
    }

    public abstract void stop(CampaignEnrollment enrollment);

    public Map<String, List<DateTime>> getCampaignTimings(DateTime startDate, DateTime endDate, CampaignEnrollment enrollment) {
        Map<String, List<DateTime>> messageTimingsMap = new HashMap<>();
        C campaign = (C) getCampaignRecordService().findByName(enrollment.getCampaignName()).build();
        for (M message : campaign.getMessages()) {
            String externalJobIdPrefix = messageJobIdFor(message.getMessageKey(), enrollment.getExternalId(), enrollment.getCampaignName());
            List<DateTime> dates = convertToDateTimeList(schedulerService.getScheduledJobTimingsWithPrefix(EventKeys.SEND_MESSAGE, externalJobIdPrefix, startDate.toDate(), endDate.toDate()));

            messageTimingsMap.put(message.getName(), dates);
        }
        return messageTimingsMap;
    }

    protected abstract void scheduleMessageJob(CampaignEnrollment enrollment, CampaignMessage message);

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
}

