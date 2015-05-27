package org.motechproject.messagecampaign.domain.message;

import org.apache.commons.lang.StringUtils;
import org.joda.time.Period;
import org.motechproject.commons.date.model.Time;

import java.util.List;

import static org.motechproject.commons.date.model.Time.parseTime;
import org.joda.time.LocalDate;
import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.messagecampaign.domain.campaign.AbsoluteCampaign;
import org.motechproject.messagecampaign.domain.campaign.Campaign;

public class CampaignMessage {
    private String name;
    private List<String> formats;
    private List<String> languages;
    private String messageKey;
    private Time startTime;
    private Campaign campaign;

    public CampaignMessage(CampaignMessageRecord messageRecord) {
        this (messageRecord.getName(), messageRecord.getFormats(), messageRecord.getLanguages(), messageRecord.getMessageKey(),
                parseTime(messageRecord.getStartTime(), ":"));
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

    public void setStartTime(String startTime) {
      if (StringUtils.isNotBlank(startTime)) {
          this.startTime = parseTime(startTime, ":");
      }
    }

    public void validate() {
        throw new RuntimeException("TODO: Not implemented");
    }

    // Former functionality of former specific subclasses





}
