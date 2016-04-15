package org.motechproject.openmrs19.domain;

import java.util.List;

/**
 * Stores a list of {@link Encounter}s. This class is used as a result of a query to the OpenMRS server.
 */
public class EncounterListResult {

    private List<Encounter> results;

    public List<Encounter> getResults() {
        return results;
    }

    public void setResults(List<Encounter> results) {
        this.results = results;
    }
}
