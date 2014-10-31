package org.motechproject.messagecampaign.scheduler;

import org.springframework.stereotype.Component;

@Component
public class JobIdFactory {

    public String getMessageJobIdFor(String campaignName, String externalId, String messageKey) {
        return String.format("MessageJob.%s.%s.%s", campaignName, externalId, messageKey);
    }

    public String campaignCompletedJobIdFor(String campaignName, String externalId) {
        return String.format("EndOfCampaignJob.%s.%s", campaignName, externalId);
    }
}
