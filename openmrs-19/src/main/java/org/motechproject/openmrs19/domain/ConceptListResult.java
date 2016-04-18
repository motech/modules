package org.motechproject.openmrs19.domain;

import java.util.List;

/**
 * Stores a list of {@link Concept}s. This class is used as a result of a query to the OpenMRS server.
 */
public class ConceptListResult {

    private List<Concept> results;

    public List<Concept> getResults() {
        return results;
    }

    public void setResults(List<Concept> results) {
        this.results = results;
    }
}
