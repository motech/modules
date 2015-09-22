package org.motechproject.scheduletracking.domain.search;

import org.motechproject.scheduletracking.domain.Enrollment;
import org.motechproject.scheduletracking.repository.AllEnrollments;

import java.util.List;

/**
 * Criteria interface provides methods to fetch records from database and to filter records in memory.
 */
public interface Criterion  {

    /**
     * Returns list of the enrollments from the database using the given DAO service.
     *
     * @param allEnrollments the DAO service used to retrieve enrollments
     * @return the list of the enrollments
     */
    List<Enrollment> fetch(AllEnrollments allEnrollments);

    /**
     * Returns filtered list of the enrollments from the given enrollments list.
     *
     * @param enrollments the enrollments to filter
     * @return the filtered list of the enrollments
     */
    List<Enrollment> filter(List<Enrollment> enrollments);
}


