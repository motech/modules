package org.motechproject.messagecampaign.scheduler;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.commons.date.model.Time;
import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.event.MotechEvent;
import org.motechproject.messagecampaign.EventKeys;
import org.motechproject.messagecampaign.dao.CampaignRecordService;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollment;
import org.motechproject.messagecampaign.domain.campaign.CampaignMessage;
import org.motechproject.messagecampaign.domain.campaign.OffsetCampaign;
import org.motechproject.messagecampaign.domain.campaign.OffsetCampaignMessage;
import org.motechproject.scheduler.contract.JobId;
import org.motechproject.scheduler.contract.RunOnceJobId;
import org.motechproject.scheduler.contract.RunOnceSchedulableJob;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.motechproject.commons.date.util.DateUtil.newDateTime;
import static org.motechproject.commons.date.util.DateUtil.now;

/**
 * Scheduler service, responsible for scheduling/unscheduling jobs for the {@link OffsetCampaign}s.
 *
 * @see OffsetCampaign
 * @see OffsetCampaignMessage
 */
@Component
public class OffsetCampaignSchedulerService extends CampaignSchedulerService<OffsetCampaignMessage, OffsetCampaign> {

    private static final Logger LOGGER = Logger.getLogger(OffsetCampaignSchedulerService.class);

    @Autowired
    public OffsetCampaignSchedulerService(MotechSchedulerService schedulerService, CampaignRecordService campaignRecordService) {
        super(schedulerService, campaignRecordService);
    }

    @Override
    protected void scheduleMessageJob(CampaignEnrollment enrollment, OffsetCampaign campaign, OffsetCampaignMessage message) {
        Time deliverTime = deliverTimeFor(enrollment, message);
        DateTime jobTime = (newDateTime(enrollment.getReferenceDate(), deliverTime)).toLocalDateTime()
                .plus(message.getTimeOffset()).toDateTime();

        if (jobTime.isAfter(now())) {
            MotechEvent motechEvent = new MotechEvent(EventKeys.SEND_MESSAGE, jobParams(message.getMessageKey(), enrollment));
            RunOnceSchedulableJob runOnceSchedulableJob = new RunOnceSchedulableJob(motechEvent, jobTime);

            try {
                getSchedulerService().scheduleRunOnceJob(runOnceSchedulableJob);
            } catch (IllegalArgumentException e) {
                LOGGER.info("Unable to schedule offset campaign message " + message.getMessageKey() + " for ID: " +
                        enrollment.getExternalId() + " enrolled in campaign: " + enrollment.getCampaignName() + " - Message date is in the past");
            }
        }
    }

    @Override
    public void unscheduleMessageJobs(CampaignEnrollment enrollment) {
        OffsetCampaign campaign = (OffsetCampaign) getCampaignRecordService().findByName(enrollment.getCampaignName()).toCampaign();
        for (OffsetCampaignMessage message : campaign.getMessages()) {
            getSchedulerService().safeUnscheduleRunOnceJob(EventKeys.SEND_MESSAGE, messageJobIdFor(message.getMessageKey(), enrollment.getExternalId(), enrollment.getCampaignName()));
        }
    }

    @Override
    public JobId getJobId(String messageKey, String externalId, String campaingName) {
        return new RunOnceJobId(EventKeys.SEND_MESSAGE, messageJobIdFor(messageKey, externalId, campaingName));
    }

    @Override
    public void unscheduleMessageJob(CampaignEnrollment enrollment, CampaignMessage message) {
        getSchedulerService().safeUnscheduleRunOnceJob(EventKeys.SEND_MESSAGE, messageJobIdFor(message.getMessageKey(), enrollment.getExternalId(), enrollment.getCampaignName()));
    }

    @Override
    protected DateTime campaignEndDate(OffsetCampaign campaign, CampaignEnrollment enrollment) {
        LocalDate startDate = enrollment.getReferenceDate();
        DateTime lastFireTime = null;

        for (OffsetCampaignMessage msg : campaign.getMessages()) {
            Time deliverTime = deliverTimeFor(enrollment, msg);
            DateTime fireDate = DateUtil.newDateTime(startDate, deliverTime).plus(msg.getTimeOffset());

            if (lastFireTime == null || fireDate.isAfter(lastFireTime)) {
                lastFireTime = fireDate;
            }
        }

        return lastFireTime;
    }
}
