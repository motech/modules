package org.motechproject.messagecampaign.scheduler;

import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.motechproject.event.MotechEvent;
import org.motechproject.messagecampaign.EventKeys;
import org.motechproject.messagecampaign.dao.CampaignRecordService;
import org.motechproject.messagecampaign.domain.campaign.AbsoluteCampaign;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollment;
import org.motechproject.messagecampaign.domain.message.AbsoluteCampaignMessage;
import org.motechproject.messagecampaign.domain.message.CampaignMessage;
import org.motechproject.scheduler.contract.JobId;
import org.motechproject.scheduler.contract.RunOnceJobId;
import org.motechproject.scheduler.contract.RunOnceSchedulableJob;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.motechproject.commons.date.util.DateUtil.newDateTime;

@Component
public class AbsoluteCampaignSchedulerService extends CampaignSchedulerService<AbsoluteCampaignMessage, AbsoluteCampaign> {

    private Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    public AbsoluteCampaignSchedulerService(MotechSchedulerService schedulerService, CampaignRecordService campaignRecordService) {
        super(schedulerService, campaignRecordService);
    }

    @Override
    protected void scheduleMessageJob(CampaignEnrollment enrollment, CampaignMessage campaignMessage) {
        Map<String, Object> params = jobParams(campaignMessage.getMessageKey(), enrollment);
        MotechEvent motechEvent = new MotechEvent(EventKeys.SEND_MESSAGE, params);
        LocalDate startDate = ((AbsoluteCampaignMessage) campaignMessage).getDate();
        RunOnceSchedulableJob runOnceSchedulableJob = new RunOnceSchedulableJob(motechEvent, newDateTime(startDate, deliverTimeFor(enrollment, campaignMessage)).toDate());

        try {
            getSchedulerService().scheduleRunOnceJob(runOnceSchedulableJob);
        } catch (IllegalArgumentException e) {
            logger.info("Unable to schedule absolute campaign message " + campaignMessage.getMessageKey() + " for ID: " + enrollment.getExternalId() + " enrolled in campaign: " + enrollment.getCampaignName() + " - Message date is in the past");
        }
    }

    @Override
    public void stop(CampaignEnrollment enrollment) {
        AbsoluteCampaign campaign = (AbsoluteCampaign) getCampaignRecordService().findByName(enrollment.getCampaignName()).build();
        for (AbsoluteCampaignMessage message : campaign.getMessages()) {
            getSchedulerService().safeUnscheduleRunOnceJob(EventKeys.SEND_MESSAGE, messageJobIdFor(message.getMessageKey(), enrollment.getExternalId(), enrollment.getCampaignName()));
        }
    }

    @Override
    public JobId getJobId(String messageKey, String externalId, String campaingName) {
        return new RunOnceJobId(EventKeys.SEND_MESSAGE, messageJobIdFor(messageKey, externalId, campaingName));
    }
}
