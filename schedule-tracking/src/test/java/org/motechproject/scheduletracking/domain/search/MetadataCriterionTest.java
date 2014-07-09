package org.motechproject.scheduletracking.domain.search;

import org.junit.Test;
import org.motechproject.scheduletracking.domain.Enrollment;
import org.motechproject.scheduletracking.domain.EnrollmentBuilder;
import org.motechproject.scheduletracking.domain.Schedule;
import org.motechproject.scheduletracking.repository.AllEnrollments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;
import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MetadataCriterionTest {

    @Test
    public void shouldFetchFromDbUsingCriteria() {
        AllEnrollments allEnrollments = mock(AllEnrollments.class);
        List<Enrollment> result = mock(List.class);
        when(allEnrollments.findByMetadataProperty("foo", "bar")).thenReturn(result);
        assertEquals(result, new MetadataCriterion("foo", "bar").fetch(allEnrollments));
    }

    @Test
    public void shouldFilterByMetadata() {
        Schedule schedule = new Schedule("my_schedule");
        List<Enrollment> enrollments = new ArrayList<Enrollment>();
        HashMap<String,String> metadata1 = new HashMap<String, String>(),metadata2 = new HashMap<String, String>(), metadata3 = new HashMap<String, String>(), metadata4 = new HashMap<String, String>();

        metadata1.put("foo","bar");
        metadata1.put("fuu", "bar");
        enrollments.add(new EnrollmentBuilder().withExternalId("entity1").withSchedule(schedule).withCurrentMilestoneName(null).withStartOfSchedule(null).withEnrolledOn(null).withPreferredAlertTime(null).withStatus(null).withMetadata(metadata1).toEnrollment());

        metadata2.put("foo", "baz");
        metadata2.put("fuu", "biz");
        enrollments.add(new EnrollmentBuilder().withExternalId("entity2").withSchedule(schedule).withCurrentMilestoneName(null).withStartOfSchedule(null).withEnrolledOn(null).withPreferredAlertTime(null).withStatus(null).withMetadata(metadata2).toEnrollment());

        metadata3.put("foo","bar");
        enrollments.add(new EnrollmentBuilder().withExternalId("entity3").withSchedule(schedule).withCurrentMilestoneName(null).withStartOfSchedule(null).withEnrolledOn(null).withPreferredAlertTime(null).withStatus(null).withMetadata(metadata3).toEnrollment());

        metadata4.put("foo", "boz");
        metadata4.put("fuu", "ber");
        enrollments.add(new EnrollmentBuilder().withExternalId("entity4").withSchedule(schedule).withCurrentMilestoneName(null).withStartOfSchedule(null).withEnrolledOn(null).withPreferredAlertTime(null).withStatus(null).withMetadata(metadata4).toEnrollment());

        enrollments.add(new EnrollmentBuilder().withExternalId("entity5").withSchedule(schedule).withCurrentMilestoneName(null).withStartOfSchedule(null).withEnrolledOn(null).withPreferredAlertTime(null).withStatus(null).withMetadata(null).toEnrollment());

        List<Enrollment> filtered = new MetadataCriterion("foo", "bar").filter(enrollments);
        assertEquals(asList(new String[]{ "entity1", "entity3" }), extract(filtered, on(Enrollment.class).getExternalId()));
    }


}
