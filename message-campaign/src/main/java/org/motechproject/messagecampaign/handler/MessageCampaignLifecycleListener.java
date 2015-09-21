package org.motechproject.messagecampaign.handler;

import org.motechproject.mds.annotations.InstanceLifecycleListener;
import org.motechproject.mds.annotations.InstanceLifecycleListenerType;
import org.motechproject.messagecampaign.domain.campaign.CampaignRecord;
import org.motechproject.messagecampaign.domain.campaign.CampaignMessageRecord;

/**
 * MDS instance lifecycle listener. The purpose of this interface is to mark methods responsible for
 * unscheduling jobs and removing enrollments on campaign deletions.
 */
public interface MessageCampaignLifecycleListener {

    /**
     * Removes all enrollments, assigned to the given campaign.
     *
     * @param campaignRecord campaign reocrd in deletion
     */
    @InstanceLifecycleListener(value = InstanceLifecycleListenerType.PRE_DELETE)
    void deleteCampaignEnrollments(CampaignRecord campaignRecord);

    /**
     * Unschedules a job, responsible for firing the event for the given campaign message.
     *
     * @param campaignMessageRecord campaign message in deletion
     */
    @InstanceLifecycleListener(value = InstanceLifecycleListenerType.PRE_DELETE)
    void deleteCampaignMessage(CampaignMessageRecord campaignMessageRecord);
}
