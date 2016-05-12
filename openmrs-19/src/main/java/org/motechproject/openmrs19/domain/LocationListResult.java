package org.motechproject.openmrs19.domain;

import java.util.List;
import java.util.Objects;

/**
 * Stores a list of {@link Location}s. This class is used as a result of a query to the OpenMRS server.
 */
public class LocationListResult {

    private List<Location> results;

    public List<Location> getResults() {
        return results;
    }

    public void setResults(List<Location> results) {
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

        if (!(o instanceof LocationListResult)) {
            return false;
        }

        LocationListResult other = (LocationListResult) o;

        return Objects.equals(results, other.results);
    }
}
