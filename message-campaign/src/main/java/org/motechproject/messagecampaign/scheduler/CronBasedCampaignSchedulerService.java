package org.motechproject.messagecampaign.scheduler;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.motechproject.event.MotechEvent;
import org.motechproject.messagecampaign.EventKeys;
import org.motechproject.messagecampaign.dao.CampaignRecordService;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollment;
import org.motechproject.messagecampaign.domain.campaign.CronBasedCampaign;
import org.motechproject.messagecampaign.domain.campaign.CampaignMessage;
import org.motechproject.messagecampaign.domain.campaign.CronBasedCampaignMessage;
import org.motechproject.scheduler.contract.CronJobId;
import org.motechproject.scheduler.contract.CronSchedulableJob;
import org.motechproject.scheduler.contract.JobId;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class CronBasedCampaignSchedulerService extends CampaignSchedulerService<CronBasedCampaignMessage, CronBasedCampaign> {

    @Autowired
    public CronBasedCampaignSchedulerService(MotechSchedulerService schedulerService, CampaignRecordService campaignRecordService) {
        super(schedulerService, campaignRecordService);
    }

    @Override
    protected void scheduleMessageJob(CampaignEnrollment enrollment, CronBasedCampaign campaign, CronBasedCampaignMessage message) {
        MotechEvent motechEvent = new MotechEvent(EventKeys.SEND_MESSAGE, jobParams(message.getMessageKey(), enrollment));

        LocalDate startDate = enrollment.getReferenceDate();
        Period maxDuration = campaign.getMaxDuration();
        Date endDate = (maxDuration == null) ? null : startDate.plus(maxDuration).toDate();

        CronSchedulableJob schedulableJob = new CronSchedulableJob(motechEvent, message.getCron(), startDate.toDate(), endDate);
        getSchedulerService().scheduleJob(schedulableJob);
    }

    @Override
    public void unscheduleMessageJobs(CampaignEnrollment enrollment) {
        CronBasedCampaign campaign = (CronBasedCampaign) getCampaignRecordService().findByName(enrollment.getCampaignName()).toCampaign();
        for (CronBasedCampaignMessage message : campaign.getMessages()) {
            getSchedulerService().safeUnscheduleJob(EventKeys.SEND_MESSAGE, messageJobIdFor(message.getMessageKey(), enrollment.getExternalId(), enrollment.getCampaignName()));
        }
    }

    @Override
    public void unscheduleMessageJob(CampaignEnrollment enrollment, CampaignMessage message) {
        getSchedulerService().safeUnscheduleJob(EventKeys.SEND_MESSAGE, messageJobIdFor(message.getMessageKey(), enrollment.getExternalId(), enrollment.getCampaignName()));
    }

    @Override
    public JobId getJobId(String messageKey, String externalId, String campaingName) {
        return new CronJobId(EventKeys.SEND_MESSAGE, messageJobIdFor(messageKey, externalId, campaingName));
    }

    @Override
    protected DateTime campaignEndDate(CronBasedCampaign campaign, CampaignEnrollment enrollment) {
        Period maxDuration = campaign.getMaxDuration();
        LocalDate endDate = enrollment.getReferenceDate().plus(maxDuration);
        DateTime endDt = endDate.toDateMidnight().toDateTime();
        DateTime startDt = enrollment.getReferenceDate().toDateTimeAtStartOfDay();

        Map<String, List<DateTime>> timings = getCampaignTimings(startDt, endDt, enrollment, campaign);

        return findLastDateTime(timings);
    }
}
