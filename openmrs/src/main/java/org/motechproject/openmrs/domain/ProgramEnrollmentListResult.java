package org.motechproject.openmrs.domain;

import java.util.List;
import java.util.Objects;

/**
 * Created by user on 13.05.16.
 */
public class ProgramEnrollmentListResult {

    private List<ProgramEnrollment> results;

    public List<ProgramEnrollment> getResults() {
        return results;
    }

    public void setResults(List<ProgramEnrollment> results) {
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

        if (!(o instanceof ProgramEnrollmentListResult)) {
            return false;
        }

        ProgramEnrollmentListResult other = (ProgramEnrollmentListResult) o;

        return Objects.equals(results, other.results);
    }
}
