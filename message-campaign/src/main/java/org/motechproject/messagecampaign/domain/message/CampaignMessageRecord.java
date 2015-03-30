package org.motechproject.messagecampaign.domain.message;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.joda.time.LocalDate;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.messagecampaign.web.util.LocalDateSerializer;

import java.util.List;
import java.util.Objects;

@Entity
public class CampaignMessageRecord {

    @JsonProperty
    private String name;
    @JsonProperty
    private List<String> formats;
    @JsonProperty
    private List<String> languages;
    @JsonProperty
    private String messageKey;
    @JsonProperty
    private String startTime;

    @JsonSerialize(using = LocalDateSerializer.class, include = JsonSerialize.Inclusion.NON_NULL)
    @JsonProperty
    private LocalDate date;

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private String timeOffset;
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private String repeatEvery;
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private String cron;
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private List<String> repeatOn;

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

    public List<String> getRepeatOn() {
        return repeatOn;
    }

    public void setRepeatOn(List<String> repeatOn) {
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
}
