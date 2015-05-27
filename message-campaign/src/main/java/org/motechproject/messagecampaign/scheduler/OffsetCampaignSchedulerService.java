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
import org.motechproject.messagecampaign.domain.campaign.OffsetCampaign;
import org.motechproject.messagecampaign.domain.message.CampaignMessage;
import org.motechproject.scheduler.contract.JobId;
import org.motechproject.scheduler.contract.RunOnceJobId;
import org.motechproject.scheduler.contract.RunOnceSchedulableJob;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.motechproject.commons.date.util.DateUtil.newDateTime;
import static org.motechproject.commons.date.util.DateUtil.now;

/**
 * SchedulerService responsible for (un)scheduling offset campaign enrollment
 */

@Component
public class OffsetCampaignSchedulerService extends CampaignSchedulerService<CampaignMessage, OffsetCampaign> {

    private static final Logger LOGGER = Logger.getLogger(OffsetCampaignSchedulerService.class);

    @Autowired
    public OffsetCampaignSchedulerService(MotechSchedulerService schedulerService, CampaignRecordService campaignRecordService) {
        super(schedulerService, campaignRecordService);
    }

    @Override
    protected void scheduleMessageJob(CampaignEnrollment enrollment, OffsetCampaign campaign, CampaignMessage message) {
        Time deliverTime = deliverTimeFor(enrollment, message);
        DateTime jobTime = (newDateTime(enrollment.getReferenceDate(), deliverTime)).toLocalDateTime()
                .plus(message.getTimeOffset()).toDateTime();

        if (jobTime.isAfter(now())) {
            MotechEvent motechEvent = new MotechEvent(EventKeys.SEND_MESSAGE, jobParams(message.getMessageKey(), enrollment));
            RunOnceSchedulableJob runOnceSchedulableJob = new RunOnceSchedulableJob(motechEvent, jobTime.toDate());

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
        for (CampaignMessage message : campaign.getMessages()) {
            getSchedulerService().safeUnscheduleRunOnceJob(EventKeys.SEND_MESSAGE, messageJobIdFor(message.getMessageKey(), enrollment.getExternalId(), enrollment.getCampaignName()));
        }
    }

    @Override
    public JobId getJobId(String messageKey, String externalId, String campaingName) {
        return new RunOnceJobId(EventKeys.SEND_MESSAGE, messageJobIdFor(messageKey, externalId, campaingName));
    }

    @Override
    protected DateTime campaignEndDate(OffsetCampaign campaign, CampaignEnrollment enrollment) {
        LocalDate startDate = enrollment.getReferenceDate();
        DateTime lastFireTime = null;

        for (CampaignMessage msg : campaign.getMessages()) {
            LocalDate fireDate = startDate.plus(msg.getTimeOffset());
            DateTime fireDt = DateUtil.newDateTime(fireDate, msg.getStartTime());

            if (lastFireTime == null || fireDt.isAfter(lastFireTime)) {
                lastFireTime = fireDt;
            }
        }

        return lastFireTime;
    }
}
