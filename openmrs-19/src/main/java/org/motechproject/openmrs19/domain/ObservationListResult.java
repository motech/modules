package org.motechproject.openmrs19.domain;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.List;

/**
 * Stores a list of {@link org.motechproject.openmrs19.domain.Observation}s. This class is used as a result of a query to the OpenMRS server.
 */
public class ObservationListResult {

    private List<Observation> results;

    public List<Observation> getResults() {
        return results;
    }

    public void setResults(List<Observation> results) {
        this.results = results;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof ObservationListResult)) {
            return false;
        }

        ObservationListResult other = (ObservationListResult) o;

        return ObjectUtils.equals(results, other.results);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(results).toHashCode();
    }
}
