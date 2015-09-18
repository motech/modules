package org.motechproject.scheduletracking.domain.search;

import org.joda.time.DateTime;
import org.motechproject.scheduletracking.domain.Enrollment;
import org.motechproject.scheduletracking.domain.WindowName;
import org.motechproject.scheduletracking.repository.AllEnrollments;
import java.util.ArrayList;
import java.util.List;

/**
 * Criterion used to filter enrollments by window names. If the current milestone is in one of the given windows
 * then this criterion is met.
 */
public class InWindowCriterion implements Criterion {

    private List<WindowName> windowNames;

    /**
     * Creates a InWindowCriterion with the windowNames attribute set to {@code windowNames}.
     *
     * @param windowNames the window names of the current milestone
     */
    public InWindowCriterion(List<WindowName> windowNames) {
        this.windowNames = windowNames;
    }

    @Override
    public List<Enrollment> fetch(AllEnrollments allEnrollments) {
        return filter(allEnrollments.retrieveAll());
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
