package org.motechproject.scheduletracking.domain.search;

import org.motechproject.scheduletracking.domain.Enrollment;
import org.motechproject.scheduletracking.repository.AllEnrollments;

import java.util.List;

public interface Criterion  {
    List<Enrollment> fetch(AllEnrollments allEnrollments);
    List<Enrollment> filter(List<Enrollment> enrollments);
}


