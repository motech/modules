package org.motechproject.messagecampaign.domain.campaign;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.joda.time.LocalDate;
import org.motechproject.mds.annotations.Access;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.NonEditable;
import org.motechproject.mds.annotations.UIDisplayable;
import org.motechproject.mds.util.SecurityMode;
import org.motechproject.messagecampaign.web.util.LocalDateSerializer;

import javax.jdo.annotations.Unique;
import java.util.List;
import java.util.Objects;

/**
 * Domain representation of a campaign message.
 */
@Entity
@Access(value = SecurityMode.PERMISSIONS, members = {"manageCampaigns"})
@Unique(name = "NAME_CAMPAIGN", members = { "name", "campaign"})
public class CampaignMessageRecord {

    /**
     * The database ID of this record.
     */
    @Field
    private Long id;

    /**
     * A name of this campaign message, that must be unique across its own campaign.
     */
    @UIDisplayable(position = 1)
    @Field(placeholder = "Name of the campaign message")
    @JsonProperty
    private String name;

    /**
     * Holds information about the formats of a message (eg. SMS, IVR).
     */
    @UIDisplayable(position = 9)
    @Field
    @JsonProperty
    private List<String> formats;

    /**
     * Holds information about the languages of the message.
     */
    @UIDisplayable(position = 10)
    @Field
    @JsonProperty
    private List<String> languages;

    /**
     * A key uniquely identifying this message.
     */
    @UIDisplayable(position = 2)
    @Field(required = true, tooltip = "A key uniquely identifying this message", placeholder = "MESSAGEKEY")
    @JsonProperty
    private String messageKey;

    /**
     * Holds the time at which the message will be sent.
     */
    @UIDisplayable(position = 3)
    @Field(tooltip = "Time to start the campaign", placeholder = "hh:mm")
    @JsonProperty
    private String startTime;

    /**
     * Holds the date of the delivery for this message. Used for the {@link AbsoluteCampaign}s.
     */
    @UIDisplayable(position = 4)
    @Field(tooltip = "Date to start the campaign", placeholder = "yyyy-mm-dd")
    @JsonSerialize(using = LocalDateSerializer.class, include = JsonSerialize.Inclusion.NON_NULL)
    @JsonProperty
    private LocalDate date;

    /**
     * Holds the time offset, from the reference time for this message. Used for the {@link OffsetCampaign}s.
     */
    @UIDisplayable(position = 5)
    @Field(tooltip = "Defines the amount of time from the reference date or current date that will elapse before the message is sent", placeholder = "1 week")
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private String timeOffset;

    /**
     * Holds the interval expression, defining every which time the message should be delivered. Used for
     * the {@link RepeatIntervalCampaign}s.
     */
    @UIDisplayable(position = 6)
    @Field(tooltip = "Period to repeat the campaign : 1 week, 1 day, etc..", placeholder = "\"1 Week\", \"9 Days\", \"12 Days\"")
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private String repeatEvery;

    /**
     * Holds the CRON expression, defining when the messages should be delivered. Used for the {@link CronBasedCampaign}s.
     */
    @UIDisplayable(position = 8)
    @Field(tooltip = "The cron expression determines the periodic schedule the messages will follow", placeholder = "0 0 12 * *")
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private String cron;

    /**
     * Holds the list of {@link DayOfWeek}s, defining at which days of a week the message should be delivered. Used for
     * the {@link DayOfWeekCampaign}s.
     */
    @UIDisplayable(position = 7)
    @Field(tooltip = "Specifies which days of the week the message will be sent on")
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private List<DayOfWeek> repeatOn;

    /**
     * Holds the type of the campaign this message is assigned to.
     */
    @UIDisplayable(position = 0)
    @Field(required = true)
    @JsonIgnore
    private CampaignType messageType;

    /**
     * The campaign this message is assigned to.
     */
    @UIDisplayable(position = 11)
    @Field
    @NonEditable
    @JsonIgnore
    private CampaignRecord campaign;

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

    public List<DayOfWeek> getRepeatOn() {
        return repeatOn;
    }

    public void setRepeatOn(List<DayOfWeek> repeatOn) {
        this.repeatOn = repeatOn;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTimeOffset() {
        return timeOffset;
    }

    public void setTimeOffset(String timeOffset) {
        this.timeOffset = timeOffset;
    }

    public String getRepeatEvery() {
        return repeatEvery;
    }

    public void setRepeatEvery(String repeatEvery) {
        this.repeatEvery = repeatEvery;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public CampaignType getMessageType() {
        return messageType;
    }

    public void setMessageType(CampaignType messageType) {
        this.messageType = messageType;
    }

    public CampaignRecord getCampaign() {
        return campaign;
    }

    public void setCampaign(CampaignRecord campaign) {
        this.campaign = campaign;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CampaignMessageRecord)) {
            return false;
        }

        CampaignMessageRecord other = (CampaignMessageRecord) o;

        return Objects.equals(cron, other.cron) && Objects.equals(date, other.date) &&
                Objects.equals(formats, other.formats) && Objects.equals(languages, other.languages) &&
                Objects.equals(messageKey, other.messageKey) && Objects.equals(name, other.name) &&
                equalTimes(other);
    }

    public boolean equalTimes(CampaignMessageRecord other) {
        return Objects.equals(startTime, other.startTime) && Objects.equals(timeOffset, other.timeOffset) &&
                Objects.equals(repeatEvery, other.repeatEvery) && Objects.equals(repeatOn, other.repeatOn);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (formats != null ? formats.hashCode() : 0);
        result = 31 * result + (languages != null ? languages.hashCode() : 0);
        result = 31 * result + (repeatOn != null ? repeatOn.hashCode() : 0);
        result = 31 * result + (messageKey != null ? messageKey.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + timeHash();
        return result;
    }

    private int timeHash() {
        int result = timeOffset != null ? timeOffset.hashCode() : 0;
        result = 31 * result + (cron != null ? cron.hashCode() : 0);
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        if (name != null && !name.isEmpty()) {
            return name + " - " + messageKey + " - " + messageType;
        } else {
            return messageKey + " - " + messageType;
        }
    }
}
