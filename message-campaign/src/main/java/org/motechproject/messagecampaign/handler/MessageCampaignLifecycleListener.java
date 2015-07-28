package org.motechproject.messagecampaign.handler;

import org.motechproject.mds.annotations.InstanceLifecycleListener;
import org.motechproject.mds.annotations.InstanceLifecycleListenerType;
import org.motechproject.messagecampaign.domain.campaign.CampaignRecord;
import org.motechproject.messagecampaign.domain.campaign.CampaignMessageRecord;

public interface MessageCampaignLifecycleListener {

    @InstanceLifecycleListener(value = InstanceLifecycleListenerType.PRE_DELETE)
    void deleteCampaignEnrollments(CampaignRecord campaignRecord);

    @InstanceLifecycleListener(value = InstanceLifecycleListenerType.PRE_DELETE)
    void deleteCampaignMessage(CampaignMessageRecord campaignMessageRecord);
}
