package org.motechproject.messagecampaign.web.model;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.joda.time.Period;
import org.motechproject.messagecampaign.domain.campaign.CampaignType;
import org.motechproject.messagecampaign.domain.message.CampaignMessageRecord;
import org.motechproject.messagecampaign.domain.campaign.CampaignRecurrence;

import java.io.Serializable;
import java.util.List;

public class CampaignDto implements Serializable {

    private static final long serialVersionUID = -4318242403037577752L;

    @JsonProperty
    private String name;

    @JsonProperty
    private CampaignType type;

    @JsonProperty
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private String maxDuration;

    @JsonProperty
    private List<CampaignMessageRecord> messages;

    public CampaignDto() {
    }

    public CampaignDto(CampaignRecurrence campaignRecurrence) {
        this.name = campaignRecurrence.getName();
        this.messages = campaignRecurrence.getMessages();
        this.type = campaignRecurrence.getCampaignType();
        this.maxDuration = campaignRecurrence.getMaxDuration().toString();
    }

    public CampaignRecurrence toCampaignRecord() {
        CampaignRecurrence campaignRecurrence = new CampaignRecurrence();

        campaignRecurrence.setName(name);
        campaignRecurrence.setMaxDuration(new Period(maxDuration));
        campaignRecurrence.setMessages(messages);
        campaignRecurrence.setCampaignType(type);

        return campaignRecurrence;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CampaignMessageRecord> getMessages() {
        return messages;
    }

    public void setMessages(List<CampaignMessageRecord> messages) {
        this.messages = messages;
    }

    public CampaignType getType() {
        return type;
    }

    public void setType(CampaignType type) {
        this.type = type;
    }

    public String getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(String maxDuration) {
        this.maxDuration = maxDuration;
    }
}
