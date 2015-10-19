package org.motechproject.mtraining.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.mtraining.domain.Quiz;

import java.util.List;
import java.util.Set;

/**
 * Interface for repository that persists simple records and allows CRUD.
 * MotechDataService base class will provide the implementation of this class as well
 * as methods for adding, deleting, saving and finding all instances.  In this class we
 * define and custom lookups we may need.
 */
public interface QuizDataService extends MotechDataService<Quiz> {

    @Lookup
    List<Quiz> findQuizzesByName(@LookupField(name = "name") String quizName);

    @Lookup
    Quiz findQuizById(@LookupField(name = "id") Long id);

    @Lookup
    List<Quiz> findQuizzesByIds(@LookupField(name = "id") Set<Long> ids);
}
