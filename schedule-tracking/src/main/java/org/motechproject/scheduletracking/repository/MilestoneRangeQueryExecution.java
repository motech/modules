package org.motechproject.scheduletracking.repository;

import org.joda.time.DateTime;
import org.motechproject.mds.query.QueryExecution;
import org.motechproject.mds.util.InstanceSecurityRestriction;
import org.motechproject.scheduletracking.domain.Enrollment;
import org.motechproject.scheduletracking.domain.EnrollmentStatus;

import javax.jdo.Query;
import java.util.List;

/**
 * The <code>MilestoneRangeQueryExecution</code> class prepares a custom MDS query. The query
 * should return all enrollments that have been completed during given time range.
 *
 * @see Enrollment
 */
public class MilestoneRangeQueryExecution implements QueryExecution<List<Enrollment>> {

    private DateTime start;
    private DateTime end;

    public MilestoneRangeQueryExecution(DateTime start, DateTime end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public List<Enrollment> execute(Query query, InstanceSecurityRestriction instanceSecurityRestriction) {
        query.setFilter("status == '" + EnrollmentStatus.COMPLETED.name() + "' && fulfillments.contains(fulfillment) && " +
                "(fulfillment.fulfillmentDateTime >= :start) && (fulfillment.fulfillmentDateTime <= :end) ");
        return (List<Enrollment>) query.execute(start, end);
    }

}
