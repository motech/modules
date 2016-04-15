package org.motechproject.openmrs19.domain;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof PatientIdentifierListResult)) {
            return false;
        }

        PatientIdentifierListResult other = (PatientIdentifierListResult) o;

        return ObjectUtils.equals(results, other.results);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(results).toHashCode();
    }
}
