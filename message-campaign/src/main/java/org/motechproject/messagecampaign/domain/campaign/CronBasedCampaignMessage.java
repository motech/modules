package org.motechproject.messagecampaign.domain.campaign;

import org.apache.commons.lang.StringUtils;
import org.motechproject.messagecampaign.exception.CampaignMessageValidationException;

import java.util.List;

/**
 * A type of a {@link CampaignMessage} sent for {@link CronBasedCampaign}s.
 * The message is sent periodically, based on the provided cron expression.
 */
public class CronBasedCampaignMessage extends CampaignMessage {

    /**
     * Cron expression used to determine the delivery dates.
     */
    private String cron;

    public CronBasedCampaignMessage(CampaignMessageRecord messageRecord) {
        super(messageRecord);
        this.cron = messageRecord.getCron();
    }

    public CronBasedCampaignMessage(String cron) {
        this(null, null, null, null, cron);
    }

    public CronBasedCampaignMessage(String name, List<String> formats, List<String> languages, String messageKey, String cron) {
        super(name, formats, languages, messageKey, null);
        this.cron = cron;
    }

    public String getCron() {
        return this.cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    /**
     * Ensures the {@link #cron} expression has been provided.
     * @throws CampaignMessageValidationException if cron expression has not been provided
     */
    @Override
    public void validate() {
        if (StringUtils.isBlank(cron)) {
            throw new CampaignMessageValidationException("Cron cannot be null or empty in " + CronBasedCampaignMessage.class.getName());
        }
    }
}
