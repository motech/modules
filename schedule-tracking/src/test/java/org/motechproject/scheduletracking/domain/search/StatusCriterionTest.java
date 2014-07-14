package org.motechproject.scheduletracking.domain.search;

import org.junit.Test;
import org.motechproject.scheduletracking.domain.Enrollment;
import org.motechproject.scheduletracking.domain.EnrollmentBuilder;
import org.motechproject.scheduletracking.domain.EnrollmentStatus;
import org.motechproject.scheduletracking.domain.Schedule;
import org.motechproject.scheduletracking.repository.AllEnrollments;

import java.util.ArrayList;
import java.util.List;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;
import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StatusCriterionTest {

    @Test
    public void shouldFilterByExternalId() {
        Schedule schedule = new Schedule("some_schedule");
        List<Enrollment> allEnrollments = new ArrayList<Enrollment>();
        allEnrollments.add(new EnrollmentBuilder().withExternalId(null).withSchedule(schedule).withCurrentMilestoneName(null).withStartOfSchedule(null).withEnrolledOn(null).withPreferredAlertTime(null).withStatus(EnrollmentStatus.COMPLETED).withMetadata(null).toEnrollment());
        allEnrollments.add(new EnrollmentBuilder().withExternalId(null).withSchedule(schedule).withCurrentMilestoneName(null).withStartOfSchedule(null).withEnrolledOn(null).withPreferredAlertTime(null).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment());
        allEnrollments.add(new EnrollmentBuilder().withExternalId(null).withSchedule(schedule).withCurrentMilestoneName(null).withStartOfSchedule(null).withEnrolledOn(null).withPreferredAlertTime(null).withStatus(EnrollmentStatus.DEFAULTED).withMetadata(null).toEnrollment());
        allEnrollments.add(new EnrollmentBuilder().withExternalId(null).withSchedule(schedule).withCurrentMilestoneName(null).withStartOfSchedule(null).withEnrolledOn(null).withPreferredAlertTime(null).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment());

        List<Enrollment> filteredEnrollments = new StatusCriterion(EnrollmentStatus.ACTIVE).filter(allEnrollments);
        assertEquals(asList(new EnrollmentStatus[]{ EnrollmentStatus.ACTIVE, EnrollmentStatus.ACTIVE}), extract(filteredEnrollments, on(Enrollment.class).getStatus()));
    }

    @Test
    public void shouldFetchByExternalIdFromDb() {
        List<Enrollment> enrollments = mock(List.class);
        AllEnrollments allEnrollments = mock(AllEnrollments.class);

        when(allEnrollments.findByStatus(EnrollmentStatus.ACTIVE)).thenReturn(enrollments);

        assertEquals(enrollments, new StatusCriterion(EnrollmentStatus.ACTIVE).fetch(allEnrollments));
    }

}
