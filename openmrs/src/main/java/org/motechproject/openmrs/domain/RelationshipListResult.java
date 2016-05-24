package org.motechproject.openmrs.domain;

import java.util.List;
import java.util.Objects;

/**
 * Stores a list of {@link Relationship}s. This class is used as a result of a query to the OpenMRS server.
 */
public class RelationshipListResult {

    private List<Relationship> results;

    public List<Relationship> getResults() {
        return results;
    }

    public void setResults(List<Relationship> results) {
        this.results = results;
    }

    @Override
    public int hashCode() {
        return Objects.hash(results);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof RelationshipListResult)) {
            return false;
        }

        RelationshipListResult other = (RelationshipListResult) o;

        return Objects.equals(results, other.results);
    }
}
