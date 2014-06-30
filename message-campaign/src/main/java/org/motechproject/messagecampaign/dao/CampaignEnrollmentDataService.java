package org.motechproject.messagecampaign.dao;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollment;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollmentStatus;

import java.util.List;

/**
 * MDS data service for {@link CampaignEnrollment}s.
 */
public interface CampaignEnrollmentDataService extends MotechDataService<CampaignEnrollment> {

    @Lookup
    List<CampaignEnrollment> findByCampaignName(@LookupField(name = "campaignName") String campaignName);

    @Lookup
    List<CampaignEnrollment> findByStatus(@LookupField(name = "status") CampaignEnrollmentStatus status);

    @Lookup
    List<CampaignEnrollment> findByExternalId(@LookupField(name = "externalId") String externalId);

    @Lookup
    CampaignEnrollment findById(@LookupField(name = "id") Long externalId);

    @Lookup
    CampaignEnrollment findByExternalIdAndCampaignName(@LookupField(name = "externalId") String externalId,
                                                       @LookupField(name = "campaignName") String campaignName);

}
