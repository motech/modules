package org.motechproject.scheduletracking.repository;

import org.motechproject.mds.query.QueryExecution;
import org.motechproject.mds.util.InstanceSecurityRestriction;
import org.motechproject.scheduletracking.domain.Enrollment;

import javax.jdo.Query;
import java.util.List;

/**
 * The <code>MapQueryExecution</code> class prepares a custom MDS query. The query should return
 * only these enrollments, that have got a given key-value entry in their metadata.
 *
 * @see Enrollment
 */
public class MapQueryExecution implements QueryExecution<List<Enrollment>> {

    private String key;
    private String value;

    public MapQueryExecution(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public List<Enrollment> execute(Query query, InstanceSecurityRestriction instanceSecurityRestriction) {
        query.setFilter("metadata.containsEntry(:key, :value)");
        return (List<Enrollment>) query.execute(key, value);
    }

}
