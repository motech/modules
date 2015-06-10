package org.motechproject.messagecampaign.scheduler;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.motechproject.event.MotechEvent;
import org.motechproject.messagecampaign.EventKeys;
import org.motechproject.messagecampaign.dao.CampaignRecordService;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollment;
import org.motechproject.messagecampaign.domain.campaign.RepeatIntervalCampaign;
import org.motechproject.messagecampaign.domain.message.RepeatIntervalCampaignMessage;
import org.motechproject.scheduler.contract.JobId;
import org.motechproject.scheduler.contract.RepeatingJobId;
import org.motechproject.scheduler.contract.RepeatingSchedulableJob;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

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
    protected void scheduleMessageJob(CampaignEnrollment enrollment, RepeatIntervalCampaign campaign, RepeatIntervalCampaignMessage message) {
        MotechEvent motechEvent = new MotechEvent(EventKeys.SEND_MESSAGE, jobParams(message.getMessageKey(), enrollment));
        DateTime start = newDateTime(enrollment.getReferenceDate(), deliverTimeFor(enrollment, message));
        DateTime end = start.plus(campaign.getMaxDuration());
        RepeatingSchedulableJob job = new RepeatingSchedulableJob()
                .setMotechEvent(motechEvent)
                .setStartTime(start.toDate())
                .setEndTime(end.toDate())
                .setRepeatIntervalInSeconds(message.getRepeatIntervalInSeconds())
                .setIgnorePastFiresAtStart(true)
                .setUseOriginalFireTimeAfterMisfire(true);
        getSchedulerService().safeScheduleRepeatingJob(job);
    }

    @Override
    public void unscheduleMessageJobs(CampaignEnrollment enrollment) {
        RepeatIntervalCampaign campaign = (RepeatIntervalCampaign) getCampaignRecordService().findByName(enrollment.getCampaignName()).toCampaign();
        for (RepeatIntervalCampaignMessage message : campaign.getMessages()) {
            getSchedulerService().safeUnscheduleRepeatingJob(EventKeys.SEND_MESSAGE, messageJobIdFor(message.getMessageKey(), enrollment.getExternalId(), enrollment.getCampaignName()));
        }
    }

    @Override
    public JobId getJobId(String messageKey, String externalId, String campaingName) {
        return new RepeatingJobId(EventKeys.SEND_MESSAGE, messageJobIdFor(messageKey, externalId, campaingName));
    }

    protected DateTime campaignEndDate(RepeatIntervalCampaign campaign, CampaignEnrollment enrollment) {
        Period maxDuration = campaign.getMaxDuration();
        LocalDate endDate = enrollment.getReferenceDate().plus(maxDuration);
        DateTime endDt = endDate.toDateMidnight().toDateTime();
        DateTime startDt = enrollment.getReferenceDate().toDateTimeAtStartOfDay();

        Map<String, List<DateTime>> timings = getCampaignTimings(startDt, endDt, enrollment, campaign);

        return findLastDateTime(timings);
    }
}
