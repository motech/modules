package org.motechproject.scheduletracking.domain.search;

import ch.lambdaj.Lambda;
import org.motechproject.scheduletracking.domain.Enrollment;
import org.motechproject.scheduletracking.repository.AllEnrollments;

import java.util.List;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static org.hamcrest.Matchers.equalTo;

/**
 * Criterion used to filter enrollments by the current milestone name.
 */
public class MilestoneCriterion implements Criterion {

    private String milestoneName;

    /**
     * Creates a MilestoneCriterion with the milestoneName attribute set to {@code milestoneName}.
     *
     * @param milestoneName the current milestone name
     */
    public MilestoneCriterion(String milestoneName) {
        this.milestoneName = milestoneName;
    }

    @Override
    public List<Enrollment> fetch(AllEnrollments allEnrollments) {
        return allEnrollments.findByCurrentMilestone(milestoneName);
    }

    @Override
    public List<Enrollment> filter(List<Enrollment> enrollments) {
        return Lambda.filter(having(on(Enrollment.class).getCurrentMilestoneName(), equalTo(milestoneName)), enrollments);
    }
}
