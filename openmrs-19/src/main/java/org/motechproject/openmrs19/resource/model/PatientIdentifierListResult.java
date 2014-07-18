package org.motechproject.openmrs19.resource.model;

import java.util.List;

public class PatientIdentifierListResult {

    private List<IdentifierType> results;

    public List<IdentifierType> getResults() {
        return results;
    }

    public void setResults(List<IdentifierType> results) {
        this.results = results;
    }
}
