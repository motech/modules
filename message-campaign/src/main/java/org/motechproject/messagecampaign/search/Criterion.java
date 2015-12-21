package org.motechproject.messagecampaign.search;

import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollment;
import org.motechproject.messagecampaign.dao.CampaignEnrollmentDataService;

import java.util.List;

/**
 * A representation of the criteria, used for fetching and filtering message campaign enrollments.
 */
public interface Criterion {

    /**
     * Fetches a list of campaign enrollments, using implemented criteria.
     *
     * @param campaignEnrollmentDataService DAO object to retrieve the data from
     * @return a list of campaign enrollments
     */
    List<CampaignEnrollment> fetch(CampaignEnrollmentDataService campaignEnrollmentDataService);

    /**
     * Performs in-memory filtering of the campaign enrollments, based on the implemented criteria.
     *
     * @param campaignEnrollments a list of records to filter
     * @return a list of campaign enrollments
     */
    List<CampaignEnrollment> filter(List<CampaignEnrollment> campaignEnrollments);
}


