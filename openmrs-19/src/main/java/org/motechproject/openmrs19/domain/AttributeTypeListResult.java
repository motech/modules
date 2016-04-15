package org.motechproject.openmrs19.domain;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.List;

/**
 * Stores a list of {@link Attribute.AttributeType}s. This class is used as a result of a query to the OpenMRS server.
 */
public class AttributeTypeListResult {

    private List<Attribute.AttributeType> results;

    public List<Attribute.AttributeType> getResults() {
        return results;
    }

    public void setResults(List<Attribute.AttributeType> results) {
        this.results = results;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof AttributeTypeListResult)) {
            return false;
        }

        AttributeTypeListResult other = (AttributeTypeListResult) o;

        return ObjectUtils.equals(results, other.results);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(results).toHashCode();
    }
}