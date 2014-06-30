package org.motechproject.messagecampaign.domain.message;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.motechproject.commons.date.model.Time;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.messagecampaign.web.util.TimeSerializer;

import java.util.List;

@Entity
public class CronBasedCampaignMessage implements CampaignMessage {

    @JsonProperty
    private String cron;

    public String getCron() {
        return this.cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }


    @JsonProperty
    private List<String> formats;
    @JsonProperty
    private List<String> languages;
    @JsonProperty
    private String name;
    @JsonProperty
    private String messageKey;
    @JsonProperty
    @JsonSerialize(using = TimeSerializer.class)
    private Time startTime;

    public String getName() {
        return name;
    }

    public List<String> getFormats() {
        return formats;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public String getMessageKey() {
        return messageKey;
    }


    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFormats(List<String> formats) {
        this.formats = formats;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public void setStartTime(int hour, int minute) {
        this.startTime = new Time(hour, minute);
    }
}
