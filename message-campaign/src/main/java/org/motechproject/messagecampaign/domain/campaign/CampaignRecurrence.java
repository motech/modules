package org.motechproject.messagecampaign.domain.campaign;

import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.Ignore;
import org.motechproject.messagecampaign.domain.message.CampaignMessageRecord;

import javax.jdo.annotations.Unique;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.joda.time.Period;

@Entity
public class CampaignRecurrence {

    @Unique
    private String name;

    @Cascade(delete = true)
    private List<CampaignMessageRecord> messages = new ArrayList<>();

    @Field(required = true)
    private CampaignType campaignType;

    private Period maxDuration;

    public Campaign toCampaign() {
        Campaign campaign = campaignType.instance();

        campaign.setMessageRecords(messages);
        campaign.setName(name);
        campaign.setCampaignRecurrence(this);

        return campaign;
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

    public CampaignType getCampaignType() {
        return campaignType;
    }

    public void setCampaignType(CampaignType type) {
        this.campaignType = type;
    }

    public Period getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(Period maxDuration) {
        this.maxDuration = maxDuration;
    }

    @Ignore
    public void updateFrom(CampaignRecurrence other) {
        name = other.name;
        messages = other.messages;
        campaignType = other.campaignType;
        maxDuration = other.maxDuration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CampaignRecurrence other = (CampaignRecurrence) o;

        return Objects.equals(this.campaignType, other.campaignType) && Objects.equals(this.maxDuration, other.maxDuration)
                && Objects.equals(this.messages, other.messages) && Objects.equals(this.name, other.name);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (messages != null ? messages.hashCode() : 0);
        result = 31 * result + (campaignType != null ? campaignType.hashCode() : 0);
        result = 31 * result + (maxDuration != null ? maxDuration.hashCode() : 0);
        return result;
    }
}
