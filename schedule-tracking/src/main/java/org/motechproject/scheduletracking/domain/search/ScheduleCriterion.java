package org.motechproject.scheduletracking.domain.search;

import org.motechproject.scheduletracking.domain.Enrollment;
import org.motechproject.scheduletracking.repository.AllEnrollments;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ScheduleCriterion implements Criterion {
    private List<String> scheduleNames;

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
