package org.motechproject.openmrs19.resource.model;

import java.util.List;

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
}
