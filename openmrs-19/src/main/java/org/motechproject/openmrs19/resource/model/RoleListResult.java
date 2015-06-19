package org.motechproject.openmrs19.resource.model;

import java.util.List;

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
}
