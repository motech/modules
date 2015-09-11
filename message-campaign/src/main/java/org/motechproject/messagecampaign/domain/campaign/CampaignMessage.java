package org.motechproject.messagecampaign.domain.campaign;

import org.apache.commons.lang.StringUtils;
import org.motechproject.commons.date.model.Time;

import java.util.List;

import static org.motechproject.commons.date.model.Time.parseTime;

/**
 * A base representation of a message, sent during a campaign. It contains the fields and methods
 * common to all types of a campaign message. Actual campaign message representations are created
 * extending this base class.
 *
 * @see {@link AbsoluteCampaignMessage}
 * @see {@link CronBasedCampaignMessage}
 * @see {@link DayOfWeekCampaignMessage}
 * @see {@link OffsetCampaignMessage}
 * @see {@link RepeatIntervalCampaignMessage}
 */
public abstract class CampaignMessage {

    /**
     * The name of the campaign message.
     */
    private String name;

    /**
     * Holds information about the formats of a message (eg. SMS, IVR).
     */
    private List<String> formats;

    /**
     * Holds information about the languages of the message.
     */
    private List<String> languages;

    /**
     * A key uniquely identifying this message.
     */
    private String messageKey;

    /**
     * Holds the {@link Time} at which the message will be sent.
     */
    private Time startTime;

    /**
     * Holds the domain representation of a campaign this message is assigned to.
     */
    private CampaignRecord campaign;

    public CampaignMessage(CampaignMessageRecord messageRecord) {
        this (messageRecord.getName(), messageRecord.getFormats(), messageRecord.getLanguages(), messageRecord.getMessageKey(),
                parseTime(messageRecord.getStartTime(), ":"));
        campaign = messageRecord.getCampaign();
    }

    public CampaignMessage(String name, List<String> formats, List<String> languages, String messageKey, Time startTime) {
        this.name = name;
        this.formats = formats;
        this.languages = languages;
        this.messageKey = messageKey;
        this.startTime = startTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getFormats() {
        return formats;
    }

    public void setFormats(List<String> formats) {
        this.formats = formats;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    /**
     * Sets the fire time for this campaign message. The time must be formatted as hh:mm
     * and must be in the 24-hour format.
     * @param startTime string representation of the time
     */
    public void setStartTime(String startTime) {
      if (StringUtils.isNotBlank(startTime)) {
          this.startTime = parseTime(startTime, ":");
      }
    }

    public CampaignRecord getCampaign() {
        return campaign;
    }

    public void setCampaign(CampaignRecord campaign) {
        this.campaign = campaign;
    }

    /**
     * Base validation method for the campaign message. Each concrete subclass must
     * provide the implementation, that validates the presence of the required fields
     * for this type of a campaign message.
     *
     * @throws org.motechproject.messagecampaign.exception.CampaignMessageValidationException
     *         if the validation of the message failed
     */
    public abstract void validate();

}
