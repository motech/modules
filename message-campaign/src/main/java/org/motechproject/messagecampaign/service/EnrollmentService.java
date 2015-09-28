package org.motechproject.messagecampaign.service;

import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollment;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollmentStatus;
import org.motechproject.messagecampaign.search.Criterion;
import org.motechproject.messagecampaign.dao.CampaignEnrollmentDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The <code>EnrollmentService</code> is used by the Message Campaign module to manage
 * campaign enrollments.
 */
@Service
public class EnrollmentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnrollmentService.class);

    @Autowired
    private CampaignEnrollmentDataService campaignEnrollmentDataService;

    /**
     * Creates or updates {@link CampaignEnrollment}. A new enrollment will be created as long, as there's no enrollment
     * with the given external ID and campaign name. The update of an enrollment is only possible if the current enrollment
     * status is set to inactive.
     *
     * @param enrollment enrollment to create or update
     * @throws IllegalArgumentException in case of an attempt to register duplicate campaign with different reference date or deliver time
     */
    @Transactional
    public void register(CampaignEnrollment enrollment) {
        CampaignEnrollment existingEnrollment = campaignEnrollmentDataService.findByExternalIdAndCampaignName(enrollment.getExternalId(), enrollment.getCampaignName());
        if (existingEnrollment == null) {
            campaignEnrollmentDataService.create(enrollment);
        } else if (existingEnrollment.getStatus() == CampaignEnrollmentStatus.INACTIVE) {
            existingEnrollment.copyFrom(enrollment);
            campaignEnrollmentDataService.update(existingEnrollment);
        } else if (Objects.equals(existingEnrollment.getReferenceDate(), enrollment.getReferenceDate()) &&
                Objects.equals(existingEnrollment.getDeliverTime(), enrollment.getDeliverTime())) {
            LOGGER.warn("Attempted to register duplicate enrollment with ExternalID {} and Campaign name {}, with the" +
                    "same reference date and deliver time: {}/{}", new Object[]{enrollment.getExternalId(),
                    enrollment.getCampaignName(), enrollment.getReferenceDate(), enrollment.getDeliverTime()});
        } else {
            throw new IllegalArgumentException(String.format("Enrollment with ExternalID %s for Campaign %s, already exists, with different " +
                    "reference date and delivery time: %s/%s", enrollment.getExternalId(), enrollment.getCampaignName(),
                    existingEnrollment.getReferenceDate(), existingEnrollment.getDeliverTime()));
        }
    }

    /**
     * Sets the enrollment status to inactive.
     *
     * @param externalId external ID of the campaign enrollment
     * @param campaignName campaign name of the enrollment
     */
    @Transactional
    public void unregister(String externalId, String campaignName) {
        CampaignEnrollment enrollment = campaignEnrollmentDataService.findByExternalIdAndCampaignName(externalId, campaignName);
        if (enrollment != null) {
            enrollment.setStatus(CampaignEnrollmentStatus.INACTIVE);
            campaignEnrollmentDataService.update(enrollment);
        }
    }

    /**
     * Sets the enrollment status to inactive.
     *
     * @param enrollment enrollment to change the status for
     */
    @Transactional
    public void unregister(CampaignEnrollment enrollment) {
        enrollment.setStatus(CampaignEnrollmentStatus.INACTIVE);
        campaignEnrollmentDataService.update(enrollment);
    }

    /**
     * Removes the given enrollment from the database.
     *
     * @param enrollment enrollment to remove
     */
    @Transactional
    public void delete(CampaignEnrollment enrollment) {
        campaignEnrollmentDataService.delete(enrollment);
    }

    /**
     * Fetches {@link CampaignEnrollment}s, based on the provided {@link CampaignEnrollmentsQuery}.
     * The primary criterion is used while fetching the records from the database. Secondary criterion
     * are used to filter the records in memory.
     *
     * @param query the query to use while looking for records
     * @return campaign enrollments matching provided query
     */
    @Transactional
    public List<CampaignEnrollment> search(CampaignEnrollmentsQuery query) {
        List<CampaignEnrollment> enrollments = new ArrayList<>();
        Criterion primaryCriterion = query.getPrimaryCriterion();
        if (primaryCriterion != null) {
            enrollments = primaryCriterion.fetch(campaignEnrollmentDataService);
        }
        for (Criterion criterion : query.getSecondaryCriteria()) {
            enrollments = criterion.filter(enrollments);
        }
        return enrollments;
    }
}
