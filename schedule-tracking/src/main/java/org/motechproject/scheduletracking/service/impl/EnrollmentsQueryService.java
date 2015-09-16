package org.motechproject.scheduletracking.service.impl;

import org.motechproject.scheduletracking.domain.Enrollment;
import org.motechproject.scheduletracking.domain.search.Criterion;
import org.motechproject.scheduletracking.repository.AllEnrollments;
import org.motechproject.scheduletracking.service.EnrollmentsQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Component used for searching enrollments.
 */
@Component
public class EnrollmentsQueryService {

    private AllEnrollments allEnrollments;

    @Autowired
    public EnrollmentsQueryService(AllEnrollments allEnrollments) {
        this.allEnrollments = allEnrollments;
    }

    /**
     * Returns enrollments which meet the given criterions. Enrollments are retrieved from database using primary criteria
     * and then result list is filtered using secondary criteria.
     *
     * @param query the enrollment query with search criteria
     * @return the list of the enrollments
     */
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
