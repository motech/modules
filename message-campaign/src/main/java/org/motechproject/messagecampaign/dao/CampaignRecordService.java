package org.motechproject.messagecampaign.dao;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.messagecampaign.domain.campaign.CampaignRecurrence;

/**
 * MDS data service for {@link CampaignRecurrence}s.
 */
public interface CampaignRecordService extends MotechDataService<CampaignRecurrence> {

    @Lookup
    CampaignRecurrence findByName(@LookupField(name = "name") String name);

}
