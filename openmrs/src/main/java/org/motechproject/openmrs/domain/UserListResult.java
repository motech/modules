package org.motechproject.openmrs.domain;

import java.util.List;
import java.util.Objects;

/**
 * Stores a list of {@link User}s. This class is used as a result of a query to the OpenMRS server.
 */
public class UserListResult {

    private List<User> results;

    public List<User> getResults() {
        return results;
    }

    public void setResults(List<User> results) {
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

        if (!(o instanceof UserListResult)) {
            return false;
        }

        UserListResult other = (UserListResult) o;

        return Objects.equals(results, other.results);
    }
}
