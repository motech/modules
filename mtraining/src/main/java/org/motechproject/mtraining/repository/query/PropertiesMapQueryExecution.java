package org.motechproject.mtraining.repository.query;

import org.apache.commons.lang.StringUtils;
import org.motechproject.mds.query.QueryExecution;
import org.motechproject.mds.util.InstanceSecurityRestriction;

import javax.jdo.Query;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * The <code>PropertiesMapQueryExecution</code> class prepares a custom MDS query. The query should return
 * only these units, that contains the given properties in their properties map.
 *
 * @see org.motechproject.mtraining.domain.Course
 * @see org.motechproject.mtraining.domain.Chapter
 * @see org.motechproject.mtraining.domain.Quiz
 * @see org.motechproject.mtraining.domain.Lesson
 */
public class PropertiesMapQueryExecution<T> implements QueryExecution<List<T>> {

    private Map<String, String> properties;

    public PropertiesMapQueryExecution(Map<String, String> properties) {
        this.properties = properties;
    }

    @Override
    public List<T> execute(Query query, InstanceSecurityRestriction restriction) {
        query.setFilter(buildPropertiesFilter(properties));
        return (List<T>) query.execute();
    }

    private String buildPropertiesFilter(Map<String, String> properties) {
        if (properties == null) {
            return "";
        }
        Collection<String> strings = new ArrayList<>();
        for(String key: properties.keySet()) {
            strings.add(String.format("(properties.containsKey('%s') && properties.get('%s') == '%s')", key, key, properties.get(key)));
        }

        return String.format("%s", StringUtils.join(strings, " && "));
    }
}
