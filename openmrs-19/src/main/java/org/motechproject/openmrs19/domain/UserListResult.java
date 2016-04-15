package org.motechproject.openmrs19.domain;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.List;

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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof UserListResult)) {
            return false;
        }

        UserListResult other = (UserListResult) o;

        return ObjectUtils.equals(results, other.results);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(results).toHashCode();
    }
}
