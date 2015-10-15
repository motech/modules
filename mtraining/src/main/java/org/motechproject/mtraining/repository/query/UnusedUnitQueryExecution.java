package org.motechproject.mtraining.repository.query;

import org.motechproject.mds.query.QueryExecution;
import org.motechproject.mds.util.InstanceSecurityRestriction;

import javax.jdo.Query;
import java.util.List;

/**
 * The <code>UnusedUnitQueryExecution</code> class prepares a custom MDS query. The query should return
 * only these units, that are not used by higher-level units, for example Lessons which doesn't have relation with
 * Chapter.
 *
 * @see org.motechproject.mtraining.domain.Course
 * @see org.motechproject.mtraining.domain.Chapter
 * @see org.motechproject.mtraining.domain.Quiz
 * @see org.motechproject.mtraining.domain.Lesson
 */
public class UnusedUnitQueryExecution <T> implements QueryExecution<List<T>> {

    private static final String FILTER = "%s == null";

    /**
     * The name of the field to check in the query.
     */
    private String parentName;

    public UnusedUnitQueryExecution(String parentName) {
        this.parentName = parentName;
    }

    @Override
    public List<T> execute(Query query, InstanceSecurityRestriction restriction) {
        query.setFilter(String.format(FILTER, parentName));
        return (List<T>) query.execute();
    }
}