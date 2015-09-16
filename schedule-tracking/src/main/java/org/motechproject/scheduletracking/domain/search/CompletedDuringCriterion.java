package org.motechproject.scheduletracking.domain.search;

import org.joda.time.DateTime;
import org.motechproject.scheduletracking.domain.Enrollment;
import org.motechproject.scheduletracking.repository.AllEnrollments;

import java.util.ArrayList;
import java.util.List;

import static org.motechproject.commons.date.util.DateUtil.inRange;

/**
 * This criterion is used to filter all enrollments that have been completed during given time range.
 */
public class CompletedDuringCriterion implements Criterion {

    private DateTime start;

    private DateTime end;

    /**
     * Creates a CompletedDuringCriterion with the start attribute set to {@code start}, the end attribute to {@code end}.
     *
     * @param start the begin of the time range
     * @param end the end of the time range
     */
    public CompletedDuringCriterion(DateTime start, DateTime end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public List<Enrollment> fetch(AllEnrollments allEnrollments) {
        return allEnrollments.completedDuring(start, end);
    }

    @Override
    public List<Enrollment> filter(List<Enrollment> enrollments) {
        List<Enrollment> filteredEnrollments = new ArrayList<Enrollment>();
        for (Enrollment enrollment : enrollments) {
            if (enrollment.isCompleted() && inRange(enrollment.getLastFulfilledDate(), start, end)) {
                filteredEnrollments.add(enrollment);
            }
        }
        return filteredEnrollments;
    }
}
