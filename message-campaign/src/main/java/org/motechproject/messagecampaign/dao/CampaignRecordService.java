package org.motechproject.messagecampaign.dao;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.messagecampaign.userspecified.CampaignRecord;

/**
 * MDS data service for {@link CampaignRecord}s.
 */
public interface CampaignRecordService extends MotechDataService<CampaignRecord> {

    @Lookup
    CampaignRecord findByName(@LookupField(name = "name") String name);
}
