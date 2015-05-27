package org.motechproject.messagecampaign.builder;

import org.motechproject.messagecampaign.domain.campaign.CampaignType;
import org.motechproject.messagecampaign.domain.message.CampaignMessageRecord;
import org.motechproject.messagecampaign.domain.campaign.CampaignRecurrence;

import java.util.ArrayList;
import java.util.List;

public final class CampaignRecordBuilder {

    public static CampaignRecurrence absoluteCampaignRecord(String name, CampaignMessageRecord absoluteCampaignMessageRecord) {
        List<CampaignMessageRecord> campaignMessageRecords = new ArrayList<CampaignMessageRecord>();
        campaignMessageRecords.add(absoluteCampaignMessageRecord);

        CampaignRecurrence record = new CampaignRecurrence();
        record.setName(name);
        record.setCampaignType(CampaignType.ABSOLUTE);
        record.setMessages(campaignMessageRecords);

        return record;
    }

    public static CampaignRecurrence offsetCampaignRecord(String name, CampaignMessageRecord offsetCampaignMessageRecord) {
        List<CampaignMessageRecord> campaignMessageRecords = new ArrayList<CampaignMessageRecord>();
        campaignMessageRecords.add(offsetCampaignMessageRecord);

        CampaignRecurrence record = new CampaignRecurrence();
        record.setName(name);
        record.setCampaignType(CampaignType.OFFSET);
        record.setMaxDuration("2 Weeks");
        record.setMessages(campaignMessageRecords);

        return record;
    }

    public static CampaignRecurrence cronBasedCampaignRecord(String name, CampaignMessageRecord cronBasedMessageRecord) {
        List<CampaignMessageRecord> campaignMessageRecords = new ArrayList<CampaignMessageRecord>();
        campaignMessageRecords.add(cronBasedMessageRecord);

        CampaignRecurrence record = new CampaignRecurrence();
        record.setName(name);
        record.setCampaignType(CampaignType.CRON);
        record.setMessages(campaignMessageRecords);

        return record;
    }
}
