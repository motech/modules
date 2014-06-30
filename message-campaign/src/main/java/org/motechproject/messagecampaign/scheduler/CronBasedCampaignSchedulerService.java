package org.motechproject.messagecampaign.scheduler;

import org.joda.time.LocalDate;
import org.motechproject.event.MotechEvent;
import org.motechproject.messagecampaign.EventKeys;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollment;
import org.motechproject.messagecampaign.domain.campaign.CronBasedCampaign;
import org.motechproject.messagecampaign.domain.message.CampaignMessage;
import org.motechproject.messagecampaign.domain.message.CronBasedCampaignMessage;
import org.motechproject.messagecampaign.dao.CampaignRecordService;
import org.motechproject.scheduler.contract.CronSchedulableJob;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CronBasedCampaignSchedulerService extends CampaignSchedulerService<CronBasedCampaignMessage, CronBasedCampaign> {

    @Autowired
    public CronBasedCampaignSchedulerService(MotechSchedulerService schedulerService, CampaignRecordService campaignRecordService) {
        super(schedulerService, campaignRecordService);
    }

    @Override
    protected void scheduleMessageJob(CampaignEnrollment enrollment, CampaignMessage message) {
        CronBasedCampaignMessage cronMessage = (CronBasedCampaignMessage) message;
        MotechEvent motechEvent = new MotechEvent(EventKeys.SEND_MESSAGE, jobParams(message.getMessageKey(), enrollment));
        LocalDate startDate = enrollment.getReferenceDate();
        CronSchedulableJob schedulableJob = new CronSchedulableJob(motechEvent, cronMessage.getCron(), startDate.toDate(), null);
        getSchedulerService().scheduleJob(schedulableJob);
    }

    @Override
    public void stop(CampaignEnrollment enrollment) {
        CronBasedCampaign campaign = (CronBasedCampaign) getCampaignRecordService().findByName(enrollment.getCampaignName()).build();
        for (CronBasedCampaignMessage message : campaign.getMessages()) {
            getSchedulerService().safeUnscheduleJob(EventKeys.SEND_MESSAGE, messageJobIdFor(message.getMessageKey(), enrollment.getExternalId(), enrollment.getCampaignName()));
        }
    }
}
