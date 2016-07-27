package org.motechproject.openmrs.domain;

import java.util.List;
import java.util.Objects;

/**
 * Stores a list of {@link ProgramEnrollment}s. This class is used as a result of a query to the OpenMRS server.
 */
public class ProgramEnrollmentListResult {

    private List<ProgramEnrollment> results;

    private Integer numberOfPrograms;

    public List<ProgramEnrollment> getResults() {
        return results;
    }

    public Integer getNumberOfPrograms() {
        return numberOfPrograms;
    }

    public void setResults(List<ProgramEnrollment> results) {
        this.results = results;
    }

    public void setNumberOfPrograms(Integer size) {
        numberOfPrograms = size;
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

        if (!(o instanceof ProgramEnrollmentListResult)) {
            return false;
        }

        ProgramEnrollmentListResult other = (ProgramEnrollmentListResult) o;

        return Objects.equals(results, other.results);
    }
}
