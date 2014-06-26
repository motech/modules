package org.motechproject.mtraining.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.mtraining.domain.Course;

import java.util.List;

/**
 * Created by kosh on 6/25/14.
 */
public interface CourseDataService extends MotechDataService<Course> {

    @Lookup
    List<Course> findCourseByName(@LookupField(name = "name") String courseName);
}
