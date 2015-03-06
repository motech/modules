package org.motechproject.messagecampaign.domain.message;

import org.apache.commons.lang.StringUtils;
import org.motechproject.messagecampaign.exception.CampaignMessageValidationException;

import java.util.List;

public class CronBasedCampaignMessage extends CampaignMessage {

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

    @Override
    public void validate() {
        if (StringUtils.isBlank(cron)) {
            throw new CampaignMessageValidationException("Cron cannot be null or empty in " + CronBasedCampaignMessage.class.getName());
        }
    }
}
