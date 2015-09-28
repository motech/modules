package org.motechproject.messagecampaign.dao;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.messagecampaign.domain.campaign.CampaignRecord;

/**
 * Data Service interface for {@link CampaignRecord}s. The implementation is generated by the
 * Motech Data Services module.
 */
public interface CampaignRecordService extends MotechDataService<CampaignRecord> {

    /**
     * Finds the {@link CampaignRecord} by its name.
     *
     * @param name unique name of the campaign record
     * @return campaign record of the given name
     */
    @Lookup
    CampaignRecord findByName(@LookupField(name = "name") String name);

}
