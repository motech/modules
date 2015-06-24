package org.motechproject.openmrs19.resource.model;

import java.util.List;

/**
 * Stores a list of {@link IdentifierType}s. This class is used as a result of a query to the OpenMRS server.
 */
public class PatientIdentifierListResult {

    private List<IdentifierType> results;

    public List<IdentifierType> getResults() {
        return results;
    }

    public void setResults(List<IdentifierType> results) {
        this.results = results;
    }
}
