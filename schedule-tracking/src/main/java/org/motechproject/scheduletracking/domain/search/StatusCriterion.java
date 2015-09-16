package org.motechproject.scheduletracking.domain.search;

import ch.lambdaj.Lambda;
import org.motechproject.scheduletracking.domain.Enrollment;
import org.motechproject.scheduletracking.domain.EnrollmentStatus;
import org.motechproject.scheduletracking.repository.AllEnrollments;

import java.util.List;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static org.hamcrest.CoreMatchers.equalTo;

/**
 * Criterion used to filter enrollments by the status.
 */
public class StatusCriterion implements Criterion {

    private EnrollmentStatus status;

    /**
     * Creates a EndOfWindowCriterion with the status attribute set to {@code status}.
     *
     * @param status the status of the enrollment
     */
    public StatusCriterion(EnrollmentStatus status) {
        this.status = status;
    }

    @Override
    public List<Enrollment> fetch(AllEnrollments allEnrollments) {
        return allEnrollments.findByStatus(status);
    }

    @Override
    public List<Enrollment> filter(List<Enrollment> enrollments) {
        return Lambda.filter(having(on(Enrollment.class).getStatus(), equalTo(status)), enrollments);
    }
}
