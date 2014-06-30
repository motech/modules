package org.motechproject.messagecampaign.domain.message;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.joda.time.LocalDate;
import org.motechproject.commons.date.model.Time;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.messagecampaign.web.util.LocalDateSerializer;
import org.motechproject.messagecampaign.web.util.TimeSerializer;

import java.util.List;

@Entity
public class AbsoluteCampaignMessage implements CampaignMessage {

    @JsonProperty
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate date;

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @JsonProperty
    @JsonSerialize(using = TimeSerializer.class)
    private Time startTime;

    @JsonProperty
    private String name;
    @JsonProperty
    private List<String> formats;
    @JsonProperty
    private String messageKey;
    @JsonProperty
    private List<String> languages;

    public String getName() {
        return name;
    }

    public List<String> getFormats() {
        return formats;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFormats(List<String> formats) {
        this.formats = formats;
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
