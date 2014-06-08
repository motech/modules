package org.motechproject.scheduletracking.domain.search;

import org.joda.time.DateTime;
import org.motechproject.scheduletracking.domain.Enrollment;
import org.motechproject.scheduletracking.repository.AllEnrollments;

import java.util.ArrayList;
import java.util.List;

import static org.motechproject.commons.date.util.DateUtil.inRange;

public class CompletedDuringCriterion implements Criterion {
    private DateTime start;
    private DateTime end;

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
