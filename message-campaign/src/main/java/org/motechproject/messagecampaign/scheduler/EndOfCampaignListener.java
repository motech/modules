package org.motechproject.messagecampaign.scheduler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.messagecampaign.EventKeys;
import org.motechproject.messagecampaign.dao.CampaignEnrollmentDataService;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollment;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollmentStatus;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EndOfCampaignListener {

    private CampaignEnrollmentDataService campaignEnrollmentDataService;

    @Autowired
    public EndOfCampaignListener(CampaignEnrollmentDataService campaignEnrollmentDataService) {
        this.campaignEnrollmentDataService = campaignEnrollmentDataService;
    }

    @MotechListener(subjects = EventKeys.CAMPAIGN_COMPLETED)
    public void handle(MotechEvent event) throws SchedulerException {
        String campaignName = (String) event.getParameters().get(EventKeys.CAMPAIGN_NAME_KEY);
        String externalId = (String) event.getParameters().get(EventKeys.EXTERNAL_ID_KEY);

        markEnrollmentAsComplete(externalId, campaignName);
    }

    private void markEnrollmentAsComplete(String externalId, String campaignName) {
        CampaignEnrollment enrollment = campaignEnrollmentDataService.findByExternalIdAndCampaignName(externalId, campaignName);
        enrollment.setStatus(CampaignEnrollmentStatus.COMPLETED);
        campaignEnrollmentDataService.update(enrollment);
    }
}
