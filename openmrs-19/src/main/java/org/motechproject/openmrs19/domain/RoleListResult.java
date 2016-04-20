package org.motechproject.openmrs19.domain;

import java.util.List;
import java.util.Objects;

/**
 * Stores a list of {@link Role}s. This class is used as a result of a query to the OpenMRS server.
 */
public class RoleListResult {

    private List<Role> results;

    public List<Role> getResults() {
        return results;
    }

    public void setResults(List<Role> results) {
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

        if (!(o instanceof RoleListResult)) {
            return false;
        }

        RoleListResult other = (RoleListResult) o;

        return Objects.equals(results, other.results);
    }
}