package org.motechproject.scheduletracking.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.scheduletracking.domain.Enrollment;
import org.motechproject.scheduletracking.domain.search.Criterion;
import org.motechproject.scheduletracking.repository.AllEnrollments;
import org.motechproject.scheduletracking.service.EnrollmentsQuery;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class EnrollmentsQueryServiceTest {

    @Mock
    private AllEnrollments allEnrollments;

    private EnrollmentsQueryService enrollmentsQueryService;

    @Before
    public void setup() {
        initMocks(this);
        enrollmentsQueryService = new EnrollmentsQueryService();
        enrollmentsQueryService.setAllEnrollments(allEnrollments);
    }

    @Test
    public void shouldFetchByPrimaryCriterionFromDbAndFilterSubsequentCriteriaInCode() {

        Criterion primaryCriterion = mock(Criterion.class);
        List<Enrollment> filteredByMetadata = mock(List.class);
        when(primaryCriterion.fetch(allEnrollments)).thenReturn(filteredByMetadata);

        Criterion secondaryCriterion1 = mock(Criterion.class);
        List<Enrollment> criterion1FilteredEnrollments = mock(List.class);
        when(secondaryCriterion1.filter(filteredByMetadata)).thenReturn(criterion1FilteredEnrollments);

        Criterion secondaryCriterion2 = mock(Criterion.class);
        List<Enrollment> expectedFilteredEnrollments = mock(List.class);
        when(secondaryCriterion2.filter(criterion1FilteredEnrollments)).thenReturn(expectedFilteredEnrollments);

        EnrollmentsQuery enrollmentQuery = mock(EnrollmentsQuery.class);
        when(enrollmentQuery.getPrimaryCriterion()).thenReturn(primaryCriterion);
        when(enrollmentQuery.getSecondaryCriteria()).thenReturn(asList(secondaryCriterion1, secondaryCriterion2));

        assertEquals(expectedFilteredEnrollments, enrollmentsQueryService.search(enrollmentQuery));

    }
}
