package org.motechproject.openmrs19.domain;

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
}