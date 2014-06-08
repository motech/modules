package org.motechproject.scheduletracking.service.impl;

import org.motechproject.scheduletracking.domain.Enrollment;
import org.motechproject.scheduletracking.domain.search.Criterion;
import org.motechproject.scheduletracking.repository.AllEnrollments;
import org.motechproject.scheduletracking.service.EnrollmentsQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EnrollmentsQueryService {

    private AllEnrollments allEnrollments;

    @Autowired
    public EnrollmentsQueryService(AllEnrollments allEnrollments) {
        this.allEnrollments = allEnrollments;
    }

    public List<Enrollment> search(EnrollmentsQuery query) {
        List<Enrollment> enrollments = new ArrayList<Enrollment>();
        Criterion primaryCriterion = query.getPrimaryCriterion();
        if (primaryCriterion != null) {
            enrollments = primaryCriterion.fetch(allEnrollments);
        }
        for (Criterion criterion : query.getSecondaryCriteria()) {
            enrollments = criterion.filter(enrollments);
        }
        return enrollments;
    }
}
