package org.motechproject.messagecampaign.scheduler;

import org.springframework.stereotype.Component;

/**
 * Responsible for generating IDs for the scheduled jobs, based on provided parameters.
 */
@Component
public class JobIdFactory {

    /**
     * Generates ID for the message job, based on the provided parameters.
     *
     * @param campaignName the name of the campaign
     * @param externalId external ID
     * @param messageKey the message key
     * @return ID for the message job
     */
    public String getMessageJobIdFor(String campaignName, String externalId, String messageKey) {
        return String.format("MessageJob.%s.%s.%s", campaignName, externalId, messageKey);
    }

    /**
     * Generates ID for the campaign completed job, based on the provided parameters.
     *
     * @param campaignName the campaign name
     * @param externalId external ID
     * @return ID for the campaign completed job
     */
    public String campaignCompletedJobIdFor(String campaignName, String externalId) {
        return String.format("EndOfCampaignJob.%s.%s", campaignName, externalId);
    }
}
