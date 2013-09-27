package org.motechproject.scheduletracking.api.domain.search;

import org.joda.time.DateTime;
import org.motechproject.scheduletracking.api.domain.Enrollment;
import org.motechproject.scheduletracking.api.domain.WindowName;
import org.motechproject.scheduletracking.api.repository.AllEnrollments;
import java.util.ArrayList;
import java.util.List;

public class InWindowCriterion implements Criterion {
    private List<WindowName> windowNames;

    public InWindowCriterion(List<WindowName> windowNames) {
        this.windowNames = windowNames;
    }

    @Override
    public List<Enrollment> fetch(AllEnrollments allEnrollments) {
        return filter(allEnrollments.getAll());
    }

    @Override
    public List<Enrollment> filter(List<Enrollment> enrollments) {
        List<Enrollment> filteredEnrollments = new ArrayList<Enrollment>();
        DateTime now = DateTime.now();
        for (Enrollment enrollment : enrollments) {
            if (windowNames.contains(enrollment.getCurrentWindowAsOf(now))) {
                filteredEnrollments.add(enrollment);
            }
        }
        return filteredEnrollments;
    }
}
