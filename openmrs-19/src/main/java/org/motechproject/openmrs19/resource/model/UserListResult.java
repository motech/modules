package org.motechproject.openmrs19.resource.model;

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
}
