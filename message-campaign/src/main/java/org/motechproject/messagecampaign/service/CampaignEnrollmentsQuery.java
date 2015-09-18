package org.motechproject.messagecampaign.service;

import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollmentStatus;
import org.motechproject.messagecampaign.search.CampaignNameCriterion;
import org.motechproject.messagecampaign.search.Criterion;
import org.motechproject.messagecampaign.search.ExternalIdCriterion;
import org.motechproject.messagecampaign.search.StatusCriterion;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the Query builder for retrieving campaign enrollments. It provides methods for different
 * query criteria. The order of criteria matters, as the first criterion is used to fetch the result from database.
 * and the other criterion are used to filter the results fetched by the first criterion (in memory).
 */
public class CampaignEnrollmentsQuery {

    private List<Criterion> criteria = new ArrayList<>();

    /**
     * Provides the method for the Status Criterion, using which, the campaign enrollments are filtered based on their status.
     *
     * @param campaignEnrollmentStatus the enrollment status to use in the criterion
     * @return this instance of a query, with Status criterion added
     */
    public CampaignEnrollmentsQuery havingState(CampaignEnrollmentStatus campaignEnrollmentStatus) {
        criteria.add(new StatusCriterion(campaignEnrollmentStatus));
        return this;
    }

    /**
     * This provides the method for the ExternalId Criterion, using which, the campaign enrollments are filtered based on External ID.
     *
     * @param externalId the external ID to use in the criterion
     * @return this instance of a query, with External ID criterion added
     */
    public CampaignEnrollmentsQuery withExternalId(String externalId) {
        criteria.add(new ExternalIdCriterion(externalId));
        return this;
    }

    /**
     * This provides the method for the CampaignName Criterion, using which, the campaign enrollments are filtered based on
     * belonging to a particular campaign.
     *
     * @param campaignName the campaign name to use in the criterion
     * @return this instance of a query, with Campaign name criterion added
     */
    public CampaignEnrollmentsQuery withCampaignName(String campaignName) {
        criteria.add(new CampaignNameCriterion(campaignName));
        return this;
    }

    /**
     * Returns all the criteria, that are present in the built query.
     *
     * @return all the added criteria
     */
    public List<Criterion> getCriteria() {
        return criteria;
    }

    /**
     * Returns the primary criterion in the built query, which is used to fetch the results from database.
     * This is the first criterion that has been added to the query or null, if no criteria have been added.
     *
     * @return primary criterion
     */
    public Criterion getPrimaryCriterion() {
        return (criteria.size() > 0) ? criteria.get(0) : null;
    }

    /**
     * Returns all the criterion other than primary criterion in the built query, which are used to filter the results
     * of the primary criterion. This is a sub list of created criteria, containing all the entries but the first one, or
     * an empty list, if there are no secondary criteria.
     *
     * @return secondary criteria
     */
    public List<Criterion> getSecondaryCriteria() {
        return (criteria.size() > 1) ? criteria.subList(1, criteria.size()) : new ArrayList<Criterion>();
    }
}
