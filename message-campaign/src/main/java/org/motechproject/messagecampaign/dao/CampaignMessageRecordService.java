package org.motechproject.messagecampaign.dao;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.messagecampaign.domain.message.CampaignMessageRecord;

import java.util.List;

/**
 * MDS data service for {@link CampaignMessageRecord}s.
 */
public interface CampaignMessageRecordService extends MotechDataService<CampaignMessageRecord> {

    @Lookup
    List<CampaignMessageRecord> findByName(@LookupField(name = "name") String name);
}
