package org.motechproject.openmrs19.resource.model;

import java.util.List;

/**
 * Stores a list of {@link Observation}s. This class is used as a result of a query to the OpenMRS server.
 */
public class ObservationListResult {

    private List<Observation> results;

    public List<Observation> getResults() {
        return results;
    }

    public void setResults(List<Observation> results) {
        this.results = results;
    }
}
