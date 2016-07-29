package org.motechproject.openmrs.domain;

import java.util.List;
import java.util.Objects;

/**
 * Stores a list of {@link ProgramEnrollment}s. This class is used as a result of a query to the OpenMRS server.
 */
public class ProgramEnrollmentListResult {

    private List<ProgramEnrollment> results;

    public List<ProgramEnrollment> getResults() {
        return results;
    }

    public ProgramEnrollment getFirstObject() {

        if (results.size() > 0) {
            return results.get(0);
        } else {
            return null;
        }
    }

    public int getNumberOfPrograms() {
        return results.size();
    }

    public void setResults(List<ProgramEnrollment> results) { this.results = results; }

    @Override
    public int hashCode() {
        return Objects.hash(results);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof ProgramEnrollmentListResult)) {
            return false;
        }

        ProgramEnrollmentListResult other = (ProgramEnrollmentListResult) o;

        return Objects.equals(results, other.results);
    }
}
