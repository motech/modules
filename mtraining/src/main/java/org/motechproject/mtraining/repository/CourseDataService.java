package org.motechproject.mtraining.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.mtraining.domain.Chapter;
import org.motechproject.mtraining.domain.Course;

import java.util.List;

/**
 * Interface for repository that persists simple records and allows CRUD.
 * MotechDataService base class will provide the implementation of this class as well
 * as methods for adding, deleting, saving and finding all instances.  In this class we
 * define and custom lookups we may need.
 */
public interface CourseDataService extends MotechDataService<Course> {

    @Lookup
    List<Course> findCourseByName(@LookupField(name = "name") String courseName);

    @Lookup
    Course findCourseById(@LookupField(name = "id") long id);

    @Lookup
    Chapter findChapterById(@LookupField(name = "id") long id);
}
