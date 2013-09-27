package org.motechproject.scheduletracking.api.domain.search;

import org.motechproject.scheduletracking.api.domain.Enrollment;
import org.motechproject.scheduletracking.api.repository.AllEnrollments;

import java.util.List;

public interface Criterion  {
    List<Enrollment> fetch(AllEnrollments allEnrollments);
    List<Enrollment> filter(List<Enrollment> enrollments);
}


