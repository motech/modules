package org.motechproject.openmrs19.domain;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.List;

/**
 * Stores a list of {@link Concept}s. This class is used as a result of a query to the OpenMRS server.
 */
public class ConceptListResult {

    private List<Concept> results;

    public List<Concept> getResults() {
        return results;
    }

    public void setResults(List<Concept> results) {
        this.results = results;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof ConceptListResult)) {
            return false;
        }

        ConceptListResult other = (ConceptListResult) o;

        return ObjectUtils.equals(results, other.results);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(results).toHashCode();
    }
}
