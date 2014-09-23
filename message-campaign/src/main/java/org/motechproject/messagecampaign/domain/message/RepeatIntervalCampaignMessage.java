package org.motechproject.messagecampaign.domain.message;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.joda.time.Period;
import org.motechproject.commons.date.model.Time;
import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Ignore;
import org.motechproject.messagecampaign.web.util.TimeSerializer;

import java.util.List;

@Entity
public class RepeatIntervalCampaignMessage implements CampaignMessage {

    private Period repeatInterval;

    public RepeatIntervalCampaignMessage(Period repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

    @Ignore
    public long getRepeatIntervalInMillis() {
        return repeatInterval.toDurationFrom(DateUtil.now()).getMillis();
    }

    @JsonProperty
    private String name;
    @JsonProperty
    private List<String> formats;
    @JsonProperty
    private List<String> languages;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setFormats(List<String> formats) {
        this.formats = formats;
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
