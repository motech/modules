package org.motechproject.messagecampaign.scheduler;

import org.joda.time.DateTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.messagecampaign.EventKeys;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollment;
import org.motechproject.messagecampaign.domain.campaign.RepeatIntervalCampaign;
import org.motechproject.messagecampaign.domain.message.CampaignMessage;
import org.motechproject.messagecampaign.domain.message.RepeatIntervalCampaignMessage;
import org.motechproject.messagecampaign.dao.CampaignRecordService;
import org.motechproject.scheduler.contract.RepeatingSchedulableJob;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.motechproject.commons.date.util.DateUtil.newDateTime;

/**
 * SchedulerService responsible for (un)scheduling repeat interval campaign enrollment
 */

@Component
public class RepeatIntervalCampaignSchedulerService extends CampaignSchedulerService<RepeatIntervalCampaignMessage, RepeatIntervalCampaign> {

    @Autowired
    public RepeatIntervalCampaignSchedulerService(MotechSchedulerService schedulerService, CampaignRecordService campaignRecordService) {
        super(schedulerService, campaignRecordService);
    }

    @Override
    protected void scheduleMessageJob(CampaignEnrollment enrollment, CampaignMessage m) {
        RepeatIntervalCampaign campaign = (RepeatIntervalCampaign) getCampaignRecordService().findByName(enrollment.getCampaignName()).build();
        RepeatIntervalCampaignMessage message = (RepeatIntervalCampaignMessage) m;
        MotechEvent motechEvent = new MotechEvent(EventKeys.SEND_MESSAGE, jobParams(message.getMessageKey(), enrollment));
        DateTime start = newDateTime(enrollment.getReferenceDate(), deliverTimeFor(enrollment, message));
        DateTime end = start.plus(campaign.maxDuration());
        RepeatingSchedulableJob job = new RepeatingSchedulableJob()
                .setMotechEvent(motechEvent)
                .setStartTime(start.toDate())
                .setEndTime(end.toDate())
                .setRepeatIntervalInMilliSeconds(message.getRepeatIntervalInMillis())
                .setIgnorePastFiresAtStart(true)
                .setUseOriginalFireTimeAfterMisfire(true);
        getSchedulerService().safeScheduleRepeatingJob(job);
    }

    @Override
    public void stop(CampaignEnrollment enrollment) {
        RepeatIntervalCampaign campaign = (RepeatIntervalCampaign) getCampaignRecordService().findByName(enrollment.getCampaignName()).build();
        for (RepeatIntervalCampaignMessage message : campaign.getMessages()) {
            getSchedulerService().safeUnscheduleRepeatingJob(EventKeys.SEND_MESSAGE, messageJobIdFor(message.getMessageKey(), enrollment.getExternalId(), enrollment.getCampaignName()));
        }
    }
}
