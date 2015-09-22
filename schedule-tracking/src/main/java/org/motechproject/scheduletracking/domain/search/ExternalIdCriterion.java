package org.motechproject.scheduletracking.domain.search;

import ch.lambdaj.Lambda;
import org.motechproject.scheduletracking.domain.Enrollment;
import org.motechproject.scheduletracking.repository.AllEnrollments;

import java.util.List;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static org.hamcrest.Matchers.equalTo;

/**
 * Criterion used to filter enrollments by the user external id.
 */
public class ExternalIdCriterion implements Criterion {

    private String externalId;

    /**
     * Creates a ExternalIdCriterion with the externalId attribute set to {@code externalId}.
     *
     * @param externalId the user external id
     */
    public ExternalIdCriterion(String externalId) {
        this.externalId = externalId;
    }

    @Override
    public List<Enrollment> fetch(AllEnrollments allEnrollments) {
        return allEnrollments.findByExternalId(externalId);
    }

    @Override
    public List<Enrollment> filter(List<Enrollment> enrollments) {
        return Lambda.filter(having(on(Enrollment.class).getExternalId(), equalTo(externalId)), enrollments);
    }
}
