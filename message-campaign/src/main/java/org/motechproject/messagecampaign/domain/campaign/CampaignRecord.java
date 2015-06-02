package org.motechproject.messagecampaign.domain.campaign;

import org.motechproject.mds.annotations.Access;
import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.Ignore;
import org.motechproject.mds.util.SecurityMode;
import org.motechproject.messagecampaign.domain.message.CampaignMessageRecord;

import javax.jdo.annotations.Unique;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Access(value = SecurityMode.PERMISSIONS, members = {"manageCampaigns"})
public class CampaignRecord {

    @Unique
    private String name;

    @Cascade(delete = true)
    private List<CampaignMessageRecord> messages = new ArrayList<>();

    @Field(required = true)
    private CampaignType campaignType;

    private String maxDuration;

    public Campaign toCampaign() {
        Campaign campaign = campaignType.instance();

        campaign.setMessageRecords(messages);
        campaign.setName(name);
        campaign.setMaxDuration(maxDuration);

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

    public String getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(String maxDuration) {
        this.maxDuration = maxDuration;
    }

    @Ignore
    public void updateFrom(CampaignRecord other) {
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

        CampaignRecord other = (CampaignRecord) o;

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
