package org.motechproject.messagecampaign.handler.impl;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.mds.util.Constants;
import org.motechproject.messagecampaign.EventKeys;
import org.motechproject.messagecampaign.domain.campaign.CampaignRecord;
import org.motechproject.messagecampaign.domain.campaign.CampaignMessageRecord;
import org.motechproject.messagecampaign.handler.MessageCampaignLifecycleListener;
import org.motechproject.messagecampaign.service.CampaignEnrollmentsQuery;
import org.motechproject.messagecampaign.service.MessageCampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Implementation of the {@link MessageCampaignLifecycleListener}, responsible for taking
 * necessary actions after the update and deletion of campaigns and messages.
 */
@Service("messageCampaignLifecycleListener")
public class MessageCampaignLifecycleListenerImpl implements MessageCampaignLifecycleListener {

    @Autowired
    private MessageCampaignService messageCampaignService;

    /**
     * Listens to the {@link EventKeys#CAMPAIGN_RECORD_UPDATED} events and updates
     * the enrollments assigned to the updated campaign.
     *
     * @param event received event
     */
    @MotechListener(subjects = { EventKeys.CAMPAIGN_RECORD_UPDATED })
    public void updateCampaignEnrollments(MotechEvent event) {
        Map<String, Object> parameters = event.getParameters();
        Long campaignId = (Long) parameters.get(Constants.MDSEvents.OBJECT_ID);
        messageCampaignService.updateEnrollments(campaignId);
    }

    @Override
    public void deleteCampaignEnrollments(CampaignRecord campaignRecord) {
        CampaignEnrollmentsQuery query = new CampaignEnrollmentsQuery().withCampaignName(campaignRecord.getName());
        messageCampaignService.stopAll(query, true);
    }

    /**
     * Listens to the {@link EventKeys#CAMPAIGN_MESSAGE_RECORD_UPDATED} events and reschedules
     * necessary jobs.
     *
     * @param event received event
     */
    @MotechListener(subjects = { EventKeys.CAMPAIGN_MESSAGE_RECORD_UPDATED })
    public void updateCampaignMessage(MotechEvent event) {
        Map<String, Object> parameters = event.getParameters();
        Long messageId = (Long) parameters.get(Constants.MDSEvents.OBJECT_ID);
        messageCampaignService.rescheduleMessageJob(messageId);
    }

    @Override
    public void deleteCampaignMessage(CampaignMessageRecord campaignMessageRecord) {
        messageCampaignService.unscheduleMessageJob(campaignMessageRecord);
    }
}
