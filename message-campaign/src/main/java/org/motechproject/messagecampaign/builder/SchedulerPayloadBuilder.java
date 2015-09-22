package org.motechproject.messagecampaign.builder;

import org.motechproject.messagecampaign.EventKeys;

import java.util.HashMap;
import java.util.Map;

/**
 * A builder, responsible for preparing the payload of the MOTECH Events, sent
 * as a part of the message campaigns.
 */
public class SchedulerPayloadBuilder {

    /**
     * The map keeping the parameters.
     */
    private Map<String, Object> params = new HashMap<>();

    /**
     * @return built parameters
     */
    public Map<String, Object> payload() {
        return params;
    }

    /**
     * @param id the job ID to include in the event
     * @return this instance, with included Job ID
     */
    public SchedulerPayloadBuilder withJobId(String id) {
        params.put(EventKeys.SCHEDULE_JOB_ID_KEY, id);
        return this;
    }

    /**
     * @param name the campaign name to include in the event
     * @return this instance, with included campaign name
     */
    public SchedulerPayloadBuilder withCampaignName(String name) {
        params.put(EventKeys.CAMPAIGN_NAME_KEY, name);
        return this;
    }

    /**
     * @param id the external ID to include in the event
     * @return this instance, with included external ID
     */
    public SchedulerPayloadBuilder withExternalId(String id) {
        params.put(EventKeys.EXTERNAL_ID_KEY, id);
        return this;
    }

    /**
     * @param messageKey the message key to include in the event
     * @return this instance, with included message key
     */
    public SchedulerPayloadBuilder withMessageKey(String messageKey) {
        params.put(EventKeys.MESSAGE_KEY, messageKey);
        return this;
    }
}
