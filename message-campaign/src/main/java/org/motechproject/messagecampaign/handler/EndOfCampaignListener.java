package org.motechproject.messagecampaign.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.messagecampaign.EventKeys;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollment;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollmentStatus;
import org.motechproject.messagecampaign.service.EnrollmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * MOTECH listener that handles the campaign completed events.
 */
@Component
public class EndOfCampaignListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(EndOfCampaignListener.class);

    private EnrollmentService enrollmentService;

    /**
     * Listens to the {@link EventKeys#CAMPAIGN_COMPLETED} events and updates the affected enrollment
     * status to {@link CampaignEnrollmentStatus#COMPLETED}.
     *
     * @param event the event to handle
     */
    @MotechListener(subjects = EventKeys.CAMPAIGN_COMPLETED)
    public void handle(MotechEvent event) {
        String campaignName = (String) event.getParameters().get(EventKeys.CAMPAIGN_NAME_KEY);
        String externalId = (String) event.getParameters().get(EventKeys.EXTERNAL_ID_KEY);

        CampaignEnrollment enrollment = enrollmentService.markEnrollmentAsCompleted(externalId, campaignName);
        if (enrollment == null) {
            LOGGER.warn("Cannot complete campaign: {}, because enrollment with id: {} doesn't exist",
                    campaignName, externalId);
        } else {
            LOGGER.info("Enrollment with external ID: {}, from campaign: {} marked as completed", externalId, campaignName);
        }
    }

    @Autowired
    public void setEnrollmentService(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }
}
