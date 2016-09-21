package org.motechproject.openmrs.domain;

import java.util.List;
import java.util.Objects;

/**
 * Stores a list of {@link Attribute}s. This class is used as a result of a query to the OpenMRS server.
 */
public class AttributeListResult {

    private List<Attribute> results;

    public List<Attribute> getResults() {
        return results;
    }

    public void setResults(List<Attribute> results) {
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

        if (!(o instanceof AttributeListResult)) {
            return false;
        }

        AttributeListResult other = (AttributeListResult) o;

        return Objects.equals(results, other.results);
    }
}
