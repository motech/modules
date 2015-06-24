package org.motechproject.openmrs19.resource.model;

import java.util.List;

/**
 * Stores a list of {@link Patient}s. This class is used as a result of a query to the OpenMRS server.
 */
public class PatientListResult {

    private List<Patient> results;

    public List<Patient> getResults() {
        return results;
    }

    public void setResults(List<Patient> results) {
        this.results = results;
    }
}
