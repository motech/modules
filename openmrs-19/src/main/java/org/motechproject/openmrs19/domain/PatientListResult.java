package org.motechproject.openmrs19.domain;

import java.util.List;
import java.util.Objects;

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

    @Override
    public int hashCode() {
        return Objects.hash(results);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof PatientListResult)) {
            return false;
        }

        PatientListResult other = (PatientListResult) o;

        return Objects.equals(results, other.results);
    }
}
