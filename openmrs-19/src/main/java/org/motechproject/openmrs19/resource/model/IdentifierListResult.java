package org.motechproject.openmrs19.resource.model;

import java.util.List;

/**
 * Stores a list of {@link Identifier}s. This class is used as a result of a query to the OpenMRS server.
 */
public class IdentifierListResult {

    private List<Identifier> results;

    public List<Identifier> getResults() {
        return results;
    }

    public void setResults(List<Identifier> results) {
        this.results = results;
    }
}
