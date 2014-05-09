package org.motechproject.scheduletracking.domain.search;

import ch.lambdaj.Lambda;
import org.motechproject.scheduletracking.domain.Enrollment;
import org.motechproject.scheduletracking.repository.AllEnrollments;

import java.util.Arrays;
import java.util.List;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static org.hamcrest.Matchers.isIn;

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
        return Lambda.filter(having(on(Enrollment.class).getScheduleName(), isIn(scheduleNames)), enrollments);
    }
}
