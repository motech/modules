package org.motechproject.openmrs.domain;

import java.util.List;
import java.util.Objects;

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

        return Objects.equals(results, other.results);
    }

    @Override
    public int hashCode() {
        return Objects.hash(results);
    }
}
