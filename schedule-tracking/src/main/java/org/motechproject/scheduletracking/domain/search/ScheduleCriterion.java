package org.motechproject.scheduletracking.domain.search;

import org.motechproject.scheduletracking.domain.Enrollment;
import org.motechproject.scheduletracking.repository.AllEnrollments;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Criterion used to filter enrollments by the schedule name. If the schedule name of the enrollment is on the given
 * list then criterion is met.
 */
public class ScheduleCriterion implements Criterion {

    private List<String> scheduleNames;

    /**
     * Creates a InWindowCriterion with the scheduleNames attribute set to {@code scheduleNames}.
     *
     * @param scheduleNames the schedule names
     */
    public ScheduleCriterion(String... scheduleNames) {
        this.scheduleNames = Arrays.asList(scheduleNames);
    }

    @Override
    public List<Enrollment> fetch(AllEnrollments allEnrollments) {
        return allEnrollments.findBySchedule(scheduleNames);
    }

    @Override
    public List<Enrollment> filter(List<Enrollment> enrollments) {
        for (Iterator it = enrollments.iterator(); it.hasNext();) {
            Enrollment enrollment = (Enrollment) it.next();
            if (!scheduleNames.contains(enrollment.getScheduleName())) {
                it.remove();
            }
        }

        return enrollments;
    }
}
