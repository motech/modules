package org.motechproject.openmrs19.domain;

import java.util.List;
import java.util.Objects;

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

    @Override
    public int hashCode() {
        return Objects.hash(results);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof EncounterListResult)) {
            return false;
        }

        EncounterListResult other = (EncounterListResult) o;

        return Objects.equals(results, other.results);
    }
}
