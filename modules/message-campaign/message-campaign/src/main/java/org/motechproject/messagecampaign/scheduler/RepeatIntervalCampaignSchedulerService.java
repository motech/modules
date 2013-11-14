package org.motechproject.messagecampaign.scheduler;

import org.joda.time.DateTime;
import org.motechproject.commons.date.model.Time;
import org.motechproject.event.MotechEvent;
import org.motechproject.messagecampaign.EventKeys;
import org.motechproject.messagecampaign.dao.AllMessageCampaigns;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollment;
import org.motechproject.messagecampaign.domain.campaign.RepeatIntervalCampaign;
import org.motechproject.messagecampaign.domain.message.CampaignMessage;
import org.motechproject.messagecampaign.domain.message.RepeatIntervalCampaignMessage;
import org.motechproject.messagecampaign.scheduler.exception.CampaignEnrollmentException;
import org.motechproject.scheduler.MotechSchedulerService;
import org.motechproject.scheduler.domain.RepeatingSchedulableJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.motechproject.commons.date.util.DateUtil.newDateTime;

@Component
public class RepeatIntervalCampaignSchedulerService extends CampaignSchedulerService<RepeatIntervalCampaignMessage, RepeatIntervalCampaign> {

    private static final int MILLIS_IN_A_DAY = 24 * 60 * 60 * 1000;

    @Autowired
    public RepeatIntervalCampaignSchedulerService(MotechSchedulerService schedulerService, AllMessageCampaigns allMessageCampaigns) {
        super(schedulerService, allMessageCampaigns);
    }

    @Override
    protected void scheduleMessageJob(CampaignEnrollment enrollment, CampaignMessage m) {
        RepeatIntervalCampaign campaign = (RepeatIntervalCampaign) getAllMessageCampaigns().getCampaign(enrollment.getCampaignName());
        RepeatIntervalCampaignMessage message = (RepeatIntervalCampaignMessage) m;
        MotechEvent motechEvent = new MotechEvent(EventKeys.SEND_MESSAGE, jobParams(message.messageKey(), enrollment));
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
    protected Time deliverTimeFor(CampaignEnrollment enrollment, CampaignMessage message) throws CampaignEnrollmentException {
        RepeatIntervalCampaignMessage repeatIntervalCampaignMessage = (RepeatIntervalCampaignMessage) message;
        if (repeatIntervalCampaignMessage.getRepeatIntervalInMillis() < MILLIS_IN_A_DAY) {
            Time referenceTime = enrollment.getReferenceTime();
            if (referenceTime == null) {
                throw new CampaignEnrollmentException(String.format("Cannot enroll %s for message campaign %s - Reference time is not provided.", enrollment.getExternalId(), message.name()));
            }
            return referenceTime;
        }
        return super.deliverTimeFor(enrollment, message);
    }

    @Override
    public void stop(CampaignEnrollment enrollment) {
        RepeatIntervalCampaign campaign = (RepeatIntervalCampaign) getAllMessageCampaigns().getCampaign(enrollment.getCampaignName());
        for (RepeatIntervalCampaignMessage message : campaign.getMessages()) {
            getSchedulerService().safeUnscheduleRepeatingJob(EventKeys.SEND_MESSAGE, messageJobIdFor(message.messageKey(), enrollment.getExternalId(), enrollment.getCampaignName()));
        }
    }
}
