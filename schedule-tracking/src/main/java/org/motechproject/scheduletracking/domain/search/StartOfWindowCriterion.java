package org.motechproject.scheduletracking.domain.search;

import org.joda.time.DateTime;
import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.scheduletracking.domain.Enrollment;
import org.motechproject.scheduletracking.domain.WindowName;
import org.motechproject.scheduletracking.repository.AllEnrollments;

import java.util.ArrayList;
import java.util.List;

/**
 * Criterion used to filter enrollments by the window start date from the the current milestone.
 */
public class StartOfWindowCriterion implements Criterion {

    private WindowName windowName;

    private DateTime start;

    private DateTime end;

    /**
     * Creates a StartOfWindowCriterion with the windowName attribute set to {@code windowName}, the start attribute set to
     * {@code start}, the end attribute to {@code end}.
     *
     * @param windowName the name of the milestone window
     * @param start the begin of the time range
     * @param end the end of the time range
     */
    public StartOfWindowCriterion(WindowName windowName, DateTime start, DateTime end) {
        this.windowName = windowName;
        this.start = start;
        this.end = end;
    }

    @Override
    public List<Enrollment> fetch(AllEnrollments allEnrollments) {
        return filter(allEnrollments.retrieveAll());
    }

    @Override
    public List<Enrollment> filter(List<Enrollment> enrollments) {
        List<Enrollment> filteredEnrollments = new ArrayList<Enrollment>();
        for (Enrollment enrollment : enrollments) {
            DateTime startOfWindowForCurrentMilestone = enrollment.getStartOfWindowForCurrentMilestone(windowName);
            if (DateUtil.inRange(startOfWindowForCurrentMilestone, start, end)) {
                filteredEnrollments.add(enrollment);
            }
        }
        return filteredEnrollments;
    }
}
